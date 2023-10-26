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
            lock.writeLock().lock();

            for (int i = 0; i < garden.length; i++) {
                for (int j = 0; j < garden[i].length; j++) {
                    if (Math.random() < 0.5) {
                        garden[i][j] = 0;
                    } else {
                        garden[i][j] = 1;
                    }
                }
            }

            lock.writeLock().unlock();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

