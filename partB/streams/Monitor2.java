package partB.streams;


import java.util.concurrent.locks.ReadWriteLock;


public class Monitor2 implements Runnable {

    private int[][] garden;
    private ReadWriteLock lock;

    public Monitor2(int[][] garden, ReadWriteLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            // Acquire a read lock to display the state of the garden on the screen
            lock.readLock().lock();

            // Clear the screen
            System.out.println("\f");

            // Display the state of the garden on the screen
            System.out.println("Garden state:");
            for (int i = 0; i < garden.length; i++) {
                for (int j = 0; j < garden[i].length; j++) {
                    if (garden[i][j] == 0) {
                        System.out.print("W "); // Wilted plant
                    } else {
                        System.out.print("H "); // Healthy plant
                    }
                }
                System.out.println();
            }

            // Release the read lock
            lock.readLock().unlock();

            // Sleep for a while
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

