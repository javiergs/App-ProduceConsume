import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class PanelTable extends JPanel implements PropertyChangeListener {
	
	private Logger logger = LoggerFactory.getLogger(PanelTable.class);
	
	private DefaultTableModel model;
	
	public PanelTable() {
		// columns
		model = new DefaultTableModel();
		model.addColumn("Id");
		model.addColumn("Type");
		model.addColumn("State");
		// table
		JTable table = new JTable(model);
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
						c.setBackground(new Color(200, 200, 200));
					} else {
						c.setBackground(Color.WHITE);
					}
					// Color only the STATE column (index 2)
					if (column == 2 && value != null) {
						String stateStr = value.toString();
						c.setBackground(colorForState(stateStr, row));
					}
				}
				return c;
			}
		});
		setLayout(new BorderLayout());
		add(new JScrollPane(table));
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("do-cleaning")) {
			model.setRowCount(0);
		} else if (evt.getPropertyName().equals("worker-state-changed")) {
			Worker worker = (Worker) evt.getNewValue();
			if (worker != null) {
				updateModel(worker.getClass().getName(), worker.getState(), worker.getId());
			}
		}
	}
	
	private void updateModel(String type, Worker.State status, int id) {
		int rowIndex = findRowIndexById(id);
		if (rowIndex == -1) return;
		String s = status.name();
		model.setValueAt(type, rowIndex, 1);
		model.setValueAt(s, rowIndex, 2);
	}
	
	private int findRowIndexById(int id) {
		for (int row = 0; row < model.getRowCount(); row++) {
			Object value = model.getValueAt(row, 0);
			if (value instanceof Integer && ((Integer) value) == id) {
				return row;
			}
		}
		// add
		model.addRow(new Object[]{id, "", ""});
		return -1;
	}
	
	private Color colorForState(String state, int row) {
		boolean isEven = (row % 2 == 0);
		switch (state) {
			case "BORN":
				return isEven ? new Color(255, 255, 255, 180)
					: new Color(230, 230, 230, 200);
			case "RUNNING":
				return isEven ? new Color(150, 255, 180, 180)
					: new Color(100, 205, 130, 200);
			case "WAITING":
				return isEven ? new Color(255, 245, 120, 180)
					: new Color(205, 195, 70, 180);
			case "EXCLUSIVE_ACCESS":
				return isEven ? new Color(255, 160, 160, 180)
					: new Color(205, 110, 110, 180);
			case "STOPPED":
				return isEven ? new Color(140, 170, 255, 180)
					: new Color(100, 130, 220, 200);
			case "DEAD":
				return isEven ? new Color(210, 210, 210, 180)
					: new Color(170, 170, 170, 200);
			default:
				return isEven ? Color.WHITE : new Color(230, 230, 230);
		}
	}
	
}
