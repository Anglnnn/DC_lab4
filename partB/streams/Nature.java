package partB.streams;

import java.util.concurrent.locks.ReadWriteLock;

public class Nature implements Runnable {

    private int[][] garden;
    private ReadWriteLock lock;

    public Nature(int[][] garden, ReadWriteLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            // Acquire a write lock to change the state of the garden
            lock.writeLock().lock();

            // Arbitrarily change the state of some plants
            for (int i = 0; i < garden.length; i++) {
                for (int j = 0; j < garden[i].length; j++) {
                    if (Math.random() < 0.5) {
                        garden[i][j] = 0; // Wilted plant
                    } else {
                        garden[i][j] = 1; // Healthy plant
                    }
                }
            }

            // Release the write lock
            lock.writeLock().unlock();

            // Sleep for a while
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

