import javax.swing.*;

/**
 *
 */
public class Main {
	
	public static void main(String[] args) {
		// configure
		String question = JOptionPane.showInputDialog("How many threads?");
		int numberThreads = Integer.parseInt(question);
		Storage.getInstance().setNumberWorkers(numberThreads);
		question = JOptionPane.showInputDialog("What size for Storage?");
		int storageSize = Integer.parseInt(question);
		question = JOptionPane.showInputDialog("Sleeping time?");
		int sleepTime = Integer.parseInt(question);
		// View
		View view = new View(numberThreads);
		view.setSize(800, 600);
		view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// world
		Worker[] workers = new Worker[numberThreads];
		// storage
		Storage.getInstance().setSize(storageSize);
		//Storage.getInstance().addObserver(view);
		
		for (int i = 0; i < workers.length; i++) {
			if (i % 2 == 0)
				workers[i] = new Producer(i, sleepTime);
			else
				workers[i] = new Consumer(i, sleepTime);
			workers[i].addPropertyChangeListener(view);
		}
		view.setVisible(true);
		for (Worker worker : workers) {
			new Thread(worker).start();
		}
	}
	
}