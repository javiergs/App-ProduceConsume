import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Control panel for managing worker threads and storage settings.
 *
 * @author javiergs
 * @version 1.1
 */
public class PanelControl extends JPanel implements ActionListener {
	
	private final JTextField threadsField;
	private final JTextField storageSizeField;
	private final JTextField sleepTimeField;
	private final JButton toggleButton;
	
	private final String RUN_SYMBOL = "▶";
	private final String STOP_SYMBOL = "⏹";
	
	private Logger logger = LoggerFactory.getLogger(PanelControl.class);
	
	public PanelControl() {
		// fields
		threadsField = new JTextField("10", 5);
		threadsField.setHorizontalAlignment(JTextField.RIGHT);
		storageSizeField = new JTextField("1", 5);
		storageSizeField.setHorizontalAlignment(JTextField.RIGHT);
		sleepTimeField = new JTextField("100", 5);
		sleepTimeField.setHorizontalAlignment(JTextField.RIGHT);
		toggleButton = new JButton(RUN_SYMBOL);
		toggleButton.addActionListener(this);
		toggleButton.requestFocus();
		// panels
		JPanel workersPanel = createLabeledFieldPanel("Number of workers:", threadsField);
		JPanel storagePanel = createLabeledFieldPanel("Storage size:", storageSizeField);
		JPanel sleepPanel = createLabeledFieldPanel("Sleep time (ms):", sleepTimeField);
		// layout
		setLayout(new GridLayout(1, 4, 5, 5));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(workersPanel);
		add(storagePanel);
		add(sleepPanel);
		add(toggleButton);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(RUN_SYMBOL)) {
			logger.info("Click on ControlPanel to start workers.");
			int storageMax = Integer.parseInt(storageSizeField.getText().trim());
			int workersMax = Integer.parseInt(threadsField.getText().trim());
			int sleepTime = Integer.parseInt(sleepTimeField.getText().trim());
			Workplace.getInstance().initialize(workersMax, sleepTime, storageMax);
			Workplace.getInstance().setActive(true);
			toggleButton.setText(STOP_SYMBOL);
		} else {
			logger.info("Click on ControlPanel to stop workers.");
			Workplace.getInstance().setActive(false);
			toggleButton.setText(RUN_SYMBOL);
		}
	}
	
	private JPanel createLabeledFieldPanel(String label, JTextField field) {
		JPanel panel = new JPanel(new BorderLayout(5, 0));
		panel.add(new JLabel(label), BorderLayout.WEST);
		panel.add(field, BorderLayout.CENTER);
		return panel;
	}
	
}