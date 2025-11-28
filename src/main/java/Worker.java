import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * An abstract worker that performs tasks in a separate thread.
 *
 * @author javiergs
 * @version 1.0
 */
public abstract class Worker implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Worker.class);

	protected int id;
	protected int sleepTime;
	protected volatile State state;
	protected final Storage storage;
	
	public enum State {
		BORN,
		RUNNING,
		STOPPED,
		DEAD,
		WAITING,
		EXCLUSIVE_ACCESS
	}
	
	private long waitingSince = -1L;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	
	public void stop() {
		setState(State.DEAD);
	}
	
	public Worker (int id, int sleepTime, Storage storage) {
		this.id = id;
		this.sleepTime = sleepTime;
		this.setState(State.BORN);
		this.storage = storage;
	}
	
	@Override
	public void run() {
		setState(State.RUNNING);
		while (state != State.DEAD) {
			// logger.info("Worker {} is state {}.", id, state);
			doWork();
		}
		logger.info("Worker {} has finished execution.", id);
	}
	
	protected void setState(State state) {
		State old = this.state;
		if (old == State.DEAD || old == state) {
			return;
		}
		this.state = state;
		if (state == State.WAITING) {
			if (waitingSince < 0) {
				waitingSince = System.currentTimeMillis();
			}
		} else {
			waitingSince = -1L;
		}
		//pcs.firePropertyChange("state", old, state);
		WorkSpace.getInstance().update(this);
	
	}
	
	public State getState () {
		return state;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract void doWork();
	
	public void doTask() {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public synchronized double getCurrentWaitingSeconds() {
		if (waitingSince < 0) {
			return 0.0;
		}
		long now = System.currentTimeMillis();
		return (now - waitingSince) / 100.0;
	}
	
	
	
	public Color getColor() {
		return getColor(this.state.name(), this.getClass().getName());
	}
	
	public static Color getColor(String state, String type) {
		boolean isEven = type.equals(Producer.class.getName());
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
