import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window that integrates the control panel and view.
 *
 * @author javiergs
 * @version 1.0
 */
public class Main extends JFrame {
	
	private final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public Main() {
		// logic
		PanelControl panelControl = new PanelControl();
		PanelTable panelTable = new PanelTable();
		PanelGrid panelGrid = new PanelGrid();
		// layout
		setLayout(new BorderLayout());
		add(panelControl, BorderLayout.NORTH);
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(new JScrollPane(panelTable), "Table View");
		tabbedPane.add(panelGrid, "Grid View");
		add(tabbedPane, BorderLayout.CENTER);
		logger.info("Main window initialized.");
	  // listeners
		Workplace.getInstance().addPropertyChangeListener(panelTable);
		Workplace.getInstance().addPropertyChangeListener(panelGrid);
		logger.info("Listeners added to WorkPlace.");
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Main main = new Main();
			main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			main.setTitle("Producer-Consumer Simulation");
			main.setSize(800, 600);
			main.setLocationRelativeTo(null);
			main.setVisible(true);
		});
	}
	
}