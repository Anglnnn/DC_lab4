package partB.streams;

import java.util.concurrent.locks.ReadWriteLock;

public class Gardener implements Runnable {

    private int[][] garden;
    private ReadWriteLock lock;

    public Gardener(int[][] garden, ReadWriteLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.readLock().lock();

            for (int i = 0; i < garden.length; i++) {
                for (int j = 0; j < garden[i].length; j++) {
                    if (garden[i][j] == 0) {
                        lock.writeLock().lock();
                        garden[i][j] = 1;
                        lock.writeLock().unlock();
                    }
                }
            }

            lock.readLock().unlock();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
