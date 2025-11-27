import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * An abstract worker that performs tasks in a separate thread.
 *
 * @author javiergs
 * @version 1.0
 */
public abstract class Worker implements Runnable {
	
	protected int id;
	protected int sleepTime;
	protected State state;
	
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
	
	public Worker (int id, int sleepTime) {
		this.id = id;
		this.sleepTime = sleepTime;
		this.setState(State.BORN);
	}
	
	@Override
	public void run() {
		setState(State.RUNNING);
		while (!state.equals(State.DEAD)) {
			doWork();
		}
		setState(State.DEAD);
	}
	
	protected void setState(State status) {
		State old = this.state;
		this.state = status;
		if (status == State.WAITING) {
			if (waitingSince < 0) {  // <--- guard: do not reset if already waiting
				waitingSince = System.currentTimeMillis();
			}
		} else {
			// Any other state: clear waiting start
			waitingSince = -1L;
		}
		pcs.firePropertyChange("state", old, status);
		
	}
	
	public State getState () {
		return state;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSleepTime() {
		return sleepTime;
	}
	
	public abstract void doWork();
	
	public void doTask() {
		for (double i = 0; i < 10000; i += 0.00001) {}
	}
	
	public synchronized double getCurrentWaitingSeconds() {
		if (waitingSince < 0) {
			return 0.0;
		}
		long now = System.currentTimeMillis();
		
		// IMPORTANT: 1000.0 so we get DOUBLE division, not integer
		return (now - waitingSince) / 10.0;
	}

	
}
