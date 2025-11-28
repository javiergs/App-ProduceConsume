import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Panel that displays a grid of workers with colors based on their states.
 *
 * @author javiergs
 * @version 2.0
 */
public class PanelGrid extends JPanel implements PropertyChangeListener {
	
	private final Map<Integer, Worker> workers = new LinkedHashMap<>();
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (Workplace.PROP_DO_CLEANING.equals(name)) {
			workers.clear();
			repaint();
		} else if (Workplace.PROP_WORKER_STATE.equals(name)) {
			Worker worker = (Worker) evt.getNewValue();
			if (worker == null) return;
			workers.put(worker.getId(), worker);
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (workers.isEmpty()) return;
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int width = getWidth();
		int height = getHeight();
		List<Integer> ids = new ArrayList<>(workers.keySet());
		Collections.sort(ids);
		int n = ids.size();
		int cols = (int) Math.ceil(Math.sqrt(n));
		int rows = (int) Math.ceil((double) n / cols);
		int cellW = width / cols;
		int cellH = height / rows;
		for (int index = 0; index < n; index++) {
			int id = ids.get(index);
			Worker worker = workers.get(id);
			int gridRow = index / cols;
			int gridCol = index % cols;
			int x = gridCol * cellW;
			int y = gridRow * cellH;
			// 0 → Producer tone, 1 → Consumer tone
			int toneRow = (worker instanceof WorkerProducer) ? 0 : 1;
			Color fill = Configure.colorForState(worker.getState(), toneRow);
			g2.setColor(fill);
			g2.fillRect(x, y, cellW, cellH);
			g2.setColor(Color.DARK_GRAY);
			g2.drawRect(x, y, cellW, cellH);
			drawCenteredText(g2, String.valueOf(id), x, y, cellW, cellH);
		}
		g2.dispose();
	}
	
	private void drawCenteredText(Graphics2D g2, String text, int x, int y, int w, int h) {
		FontMetrics fm = g2.getFontMetrics();
		int tw = fm.stringWidth(text);
		int th = fm.getAscent();
		int tx = x + (w - tw) / 2;
		int ty = y + (h + th) / 2 - 2;
		g2.setColor(Color.BLACK);
		g2.drawString(text, tx, ty);
	}
	
}