/**
 * A producer that generates a value and stores it in the shared storage
 *
 * @author javiergs
 * @version 1.0
 */
public class Producer extends Worker {
	
	public Producer(int id, int sleepTime) {
		super(id, sleepTime);
	}
	
	@Override
	public void doWork() {
		doTask();
		int value = (int) Math.floor( Math.random() * 100 );
		Storage.getInstance().put(this, value);
		doTask();
	}

}
