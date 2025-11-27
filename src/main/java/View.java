import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View class that displays the state of workers in a table and a grid.
 *
 * @author javiergs
 * @version 1.0
 */
public class View extends JFrame implements PropertyChangeListener {
	
	private DefaultTableModel model;
	private GameZone canvas;
	
	public View(int number) {
		// columns
		model = new DefaultTableModel();
		model.addColumn("Id");
		model.addColumn("Type");
		model.addColumn("State");
		// rows
		for (int i = 0; i < Storage.getInstance().getNumberOfWorkers(); i++) {
			model.addRow(new Object[]{i, "", ""});
		}
		// table
		JTable table = new JTable(model);
		canvas = new GameZone(number);
		// renderer
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					// Base striped background
					if (row % 2 == 0) {
						c.setBackground(new Color(235, 245, 255));
					} else {
						c.setBackground(Color.WHITE);
					}
					// Color only the STATE column (index 2)
					if (column == 2 && value != null) {
						String stateStr = value.toString();
						c.setBackground(colorForState(stateStr));
					}
				}
				return c;
			}
		});
		// view
		setLayout(new GridLayout(1, 1));
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(new JScrollPane(table), "Table View");
		// tabbedPane.add(canvas, "Grid View");
		add(tabbedPane);
		
	}
	
	private Color colorForState(String stateStr) {
		switch (stateStr) {
			case "BORN":
				return new Color(99, 149, 255);   // light blue
			case "RUNNING":
				return new Color(172, 248, 199);   // light green
			case "WAITING":
				return new Color(255, 255, 0);   // light yellow
			case "EXCLUSIVE_ACCESS":
				return new Color(238, 120, 120);   // light red
			case "STOPPED":
				return new Color(200, 200, 200);   // light grey
			case "DEAD":
				return new Color(63, 63, 63);   // darker grey
			default:
				return Color.WHITE;
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Worker worker = (Worker) evt.getSource();
		if (!worker.getState().equals(Worker.State.WAITING))
			updateModel(worker.getClass().getName(), worker.getState(), worker.getId(), 0);
		else
			updateModel(worker.getClass().getName(), Worker.State.WAITING, worker.getId(), worker.getCurrentWaitingSeconds());
		//canvas.draw(worker.getClass(), worker.getState(),  worker.getId());
		System.out.println("View: Worker " + worker.getClass().getName() + " " + worker.getId() + " changed state to " + worker.getState());
	}
	
	private void updateModel(String type, Worker.State status, int id, double waitingSeconds) {
		int rowIndex = findRowIndexById(id);
		if (rowIndex == -1) {
			return;
		}
		String s = status + "";
		if (status == Worker.State.WAITING) {
			s += " (" + waitingSeconds + "s)";
		}
		model.setValueAt(type, rowIndex, 1);    // TYPE
		model.setValueAt(s, rowIndex, 2);  // STATE (enum; renderer uses toString())
	}
	
	private int findRowIndexById(int id) {
		for (int row = 0; row < model.getRowCount(); row++) {
			Object value = model.getValueAt(row, 0);
			if (value instanceof Integer && ((Integer) value) == id) {
				return row;
			}
		}
		return -1;
	}
	
}
