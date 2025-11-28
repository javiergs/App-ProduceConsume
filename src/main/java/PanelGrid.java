import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 *
 */
public class PanelGrid extends JPanel  implements PropertyChangeListener {

  private Worker.State[] statuses;
  private String [] types;
	
	public PanelGrid() {
	
	}
  public PanelGrid(int number) {
    this.statuses = new Worker.State[number];
    this.types = new String[number];
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    int width = getWidth(), height = getHeight(), level;
    for (int i=0; i<types.length; i++) {
			if (statuses!=null) {
      switch(statuses[i]) {
        case WAITING: g.setColor(Color.YELLOW); level = 3; break;
        case BORN:  g.setColor(Color.PINK); level = 1;   break;
        case RUNNING:   g.setColor(Color.GREEN); level = 2;  break;
        case STOPPED:  g.setColor(Color.RED); level = 4;    break;
        case DEAD:  g.setColor(Color.BLACK); level = 5;  break;
        default:    g.setColor(Color.BLUE); level = 6;   break;
      }
      g.fillRect(i*width/types.length, height-level*(height/10), width/types.length-2, height);
    }}
  }

  public void draw (String type, Worker.State status, int id) {
    statuses[id] = status;
    types[id] = type;
    repaint();
  }
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	
	}
}