/**
 * A producer worker that generates data and puts it into storage.
 *
 * @author javiergs
 * @version 2.0
 */
public class WorkerProducer extends Worker {
	
	public WorkerProducer(int id, int sleepTime, Storage storage) {
		super(id, sleepTime, storage);
	}
	
	@Override
	public void doWork() {
		doTask();
		int value = (int) Math.floor(Math.random() * 100);
		storage.put(this, value);
		doTask();
	}
	
}
