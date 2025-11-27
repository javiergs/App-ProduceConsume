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
	
	private static Storage instance;
	private LinkedList buffer;
	private int size;
	private Lock lock;
	private Condition emptyBuffer;
	private Condition fullBuffer;
	
	private int numberOfWorkers;
	
  private Storage() {
    size = 1;
		buffer = new LinkedList();
		lock = new ReentrantLock(true);
		emptyBuffer = lock.newCondition();
		fullBuffer = lock.newCondition();
  }
	
	public static Storage getInstance() {
		if (instance == null)
			instance = new Storage();
		return instance;
	}
	
	public void put (Worker worker, int data) {
    lock.lock();
		
			try {
				while (buffer.size() == size) {
					try {
						worker.setState(Worker.State.WAITING);
						fullBuffer.await();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				worker.setState(Worker.State.EXCLUSIVE_ACCESS);
				worker.doTask();
				buffer.addLast(data);
				emptyBuffer.signalAll();
			} finally {
				lock.unlock();
				worker.setState(Worker.State.RUNNING);
			}
  }

  public void get(Worker worker) {
    lock.lock();
    while (buffer.isEmpty()) {
			try {
				worker.setState(Worker.State.WAITING);
				emptyBuffer.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		worker.setState(Worker.State.EXCLUSIVE_ACCESS);
		worker.doTask();
    buffer.removeFirst();
    fullBuffer.signalAll();
    lock.unlock();
		worker.setState(Worker.State.RUNNING);
  }
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setNumberWorkers(int numberThreads) {
		this.numberOfWorkers = numberThreads;
	}
	
	public int getNumberOfWorkers() {
		return numberOfWorkers;
	}
}
