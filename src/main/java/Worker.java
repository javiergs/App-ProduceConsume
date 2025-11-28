/**
 * An abstract worker that performs tasks in a separate thread.
 *
 * @author javiergs
 * @version 1.0
 */
public abstract class Worker implements Runnable {
	
	protected final Storage storage;
	
	protected int id;
	protected int sleepTime;
	protected volatile State state;
	
	public enum State {
		BORN,
		RUNNING,
		STOPPED,
		DEAD,
		WAITING,
		EXCLUSIVE_ACCESS
	}
	
	public Worker(int id, int sleepTime, Storage storage) {
		this.id = id;
		this.sleepTime = sleepTime;
		this.setState(State.BORN);
		this.storage = storage;
	}
	
	@Override
	public void run() {
		setState(State.RUNNING);
		while (state != State.DEAD) {
			doWork();
		}
	}
	
	public abstract void doWork();
	
	public void doTask() {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void stop() {
		setState(State.DEAD);
	}
	
	protected void setState(State state) {
		State old = this.state;
		if (old == State.DEAD || old == state) {
			return;
		}
		this.state = state;
		// notify the WorkSpace about the state change
		Workplace.getInstance().update(this);
	}
	
	public State getState() {
		return state;
	}
	
	public int getId() {
		return id;
	}
	
}
