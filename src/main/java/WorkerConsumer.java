/**
 * A worker that consumes items from storage.
 *
 * @author javiergs
 * @version 2.0
 */
public class WorkerConsumer extends Worker {
	
	public WorkerConsumer(int id, int sleepTime, Storage storage) {
		super(id, sleepTime, storage);
	}
	
	@Override
	public void doWork() {
		doTask();
		storage.get(this);
		doTask();
	}
	
}