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
 * @version 2.0
 */
public class PanelTable extends JPanel implements PropertyChangeListener {
	
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
						Worker.State state = Worker.State.valueOf(value.toString());
						c.setBackground(Configure.colorForState(state, row));
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
		if (evt.getPropertyName().equals(Workplace.PROP_DO_CLEANING)) {
			SwingUtilities.invokeLater(() ->
				model.setRowCount(0)
			);
		} else if (evt.getPropertyName().equals(Workplace.PROP_WORKER_STATE)) {
			Worker worker = (Worker) evt.getNewValue();
			if (worker != null) {
				SwingUtilities.invokeLater(() ->
					updateModel(worker.getClass().getName(), worker.getState(), worker.getId())
				);
			}
		}
	}
	
	private void updateModel(String type, Worker.State status, int id) {
		int rowIndex = findRowIndexById(id);
		if (rowIndex < 0 || rowIndex >= model.getRowCount())  return;
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
		int newRow = model.getRowCount();
		model.addRow(new Object[]{id, "", ""});
		return newRow;
	}
	
}
