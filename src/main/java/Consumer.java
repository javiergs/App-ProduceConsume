/**
 * A Worker responsible for consuming items from a shared repository
 *
 * @author javiergs
 * @version 1.0
 */
public class Consumer extends Worker {
	
	public Consumer(int id, int sleepTime) {
		super(id, sleepTime);
	}
	
	@Override
	public void doWork() {
		doTask();
		Storage.getInstance().get(this);
		doTask();
	}
	
}
