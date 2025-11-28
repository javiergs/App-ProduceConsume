import java.beans.PropertyChangeSupport;

/**
 * A thread-safe shared resource that facilitates data storage and retrieval.
 *
 * @author javiergs
 * @version 2.0
 */
public class Workplace extends PropertyChangeSupport {
	
	private static Workplace instance;
	
	private Storage storage;
	private Worker[] workers;
	
	private Workplace() {
		super(new Object());
	}
	
	public static Workplace getInstance() {
		if (instance == null)
			instance = new Workplace();
		return instance;
	}
	
	public void setActive(boolean running) {
		if (running) {
			start();
		} else {
			stop();
		}
	}
	
	public void initialize(int workerCount, int sleepTime, int storageSize) {
		storage = new Storage(storageSize);
		workers = new Worker[workerCount];
		for (int id = 0; id < workers.length; id++) {
			if (id % 2 == 0)
				workers[id] = new WorkerProducer(id, sleepTime, storage);
			else
				workers[id] = new WorkerConsumer(id, sleepTime, storage);
		}
	}
	
	private void start() {
		firePropertyChange("do-cleaning", null, true);
		for (Worker worker : workers) {
			String threadName = worker.getClass().getName() + "" + worker.getId();
			new Thread(worker, threadName).start();
		}
	}
	
	public void update(Worker worker) {
		firePropertyChange("worker-state-changed", null, worker);
	}
	
	private void stop() {
		for (Worker worker : workers) {
			worker.stop();
		}
	}
	
}