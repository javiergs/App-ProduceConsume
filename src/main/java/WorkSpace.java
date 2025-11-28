import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeSupport;

/**
 * A thread-safe shared resource that facilitates data storage and retrieval.
 *
 * @author javiergs
 * @version 1.0
 */
public class WorkSpace extends PropertyChangeSupport {
	
	private Logger logger = LoggerFactory.getLogger(WorkSpace.class);
	
	private static WorkSpace instance;
	private Storage storage;
	private Worker[] workers;
	
	private WorkSpace() {
		super(new Object());
	}
	
	public static WorkSpace getInstance() {
		if (instance == null)
			instance = new WorkSpace();
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
		logger.info("Initializing WorkPlace.");
		storage = new Storage(storageSize);
		workers = new Worker[workerCount];
		for (int id = 0; id < workers.length; id++) {
			if (id % 2 == 0)
				workers[id] = new Producer(id, sleepTime, storage);
			else
				workers[id] = new Consumer(id, sleepTime, storage);
		}
	}
	
	private void start() {
		logger.info("Starting WorkPlace.");
		firePropertyChange("do-cleaning", null, true);
		for (Worker worker : workers) {
			String threadName = worker.getClass().getName() + "" + worker.getId();
			new Thread(worker, threadName).start();
		}
	}
	
	private void stop() {
		logger.info("Stopping WorkPlace.");
		for (Worker worker : workers) {
			worker.stop();
		}
	}
	
	public void update(Worker worker) {
		firePropertyChange("worker-state-changed", null, worker);
	}
	
}
