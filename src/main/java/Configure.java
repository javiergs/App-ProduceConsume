import java.awt.*;

/**
 * Helper for mapping worker states to colors.
 *
 * @author javiergs
 * @version 1.0
 */
public class Configure {
	
	public static Color colorForState(Worker.State state, int row) {
		boolean isEven = (row % 2 == 0);
		switch (state) {
			case BORN:
				return isEven ? new Color(255, 255, 255, 180)
					: new Color(230, 230, 230, 200);
			case RUNNING:
				return isEven ? new Color(150, 255, 180, 180)
					: new Color(100, 205, 130, 200);
			case WAITING:
				return isEven ? new Color(255, 245, 120, 180)
					: new Color(205, 195, 70, 180);
			case EXCLUSIVE_ACCESS:
				return isEven ? new Color(255, 160, 160, 180)
					: new Color(205, 110, 110, 180);
			case STOPPED:
				return isEven ? new Color(140, 170, 255, 180)
					: new Color(100, 130, 220, 200);
			case DEAD:
				return isEven ? new Color(210, 210, 210, 180)
					: new Color(170, 170, 170, 200);
			default:
				return isEven ? Color.WHITE : new Color(230, 230, 230);
		}
	}
	
}
