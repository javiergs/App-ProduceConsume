/**
 * A Worker responsible for consuming items from a shared repository
 *
 * @author javiergs
 * @version 1.0
 */
public class Consumer extends Worker {
	
	public Consumer(int id, int sleepTime, Storage storage) {
		super(id, sleepTime, storage);
	}
	
	@Override
	public void doWork() {
		doTask();
		storage.get(this);
		doTask();
	}
	
}
