import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe shared resource that facilitates data storage and retrieval.
 *
 * @author javiergs
 * @version 1.0
 */
public class Storage {
	
	private Logger logger = LoggerFactory.getLogger(Storage.class);
	
	private LinkedList<Integer> buffer;
	private int size;
	
	private Lock lock;
	private Condition emptyBuffer;
	private Condition fullBuffer;
	
	public Storage(int size) {
		this.size = size;
		this.buffer = new LinkedList<>();
		this.lock = new ReentrantLock(true);
		this.emptyBuffer = lock.newCondition();
		this.fullBuffer = lock.newCondition();
	}
	
	public void put(Worker worker, int data) {
		lock.lock();
		try {
			while (buffer.size() == size) {
				worker.setState(Worker.State.WAITING);
				fullBuffer.await();
			}
			worker.setState(Worker.State.EXCLUSIVE_ACCESS);
			worker.doTask();
			buffer.addLast(data);
			emptyBuffer.signalAll();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		lock.unlock();
		worker.setState(Worker.State.RUNNING);
	}
	
	public void get(Worker worker) {
		lock.lock();
		try {
			while (buffer.isEmpty()) {
				worker.setState(Worker.State.WAITING);
				emptyBuffer.await();
			}
			worker.setState(Worker.State.EXCLUSIVE_ACCESS);
			worker.doTask();
			buffer.removeFirst();
			fullBuffer.signalAll();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		lock.unlock();
		worker.setState(Worker.State.RUNNING);
	}
	
}
