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
            lock.readLock().lock();

            System.out.println("\f");

            System.out.println("Garden state:");
            for (int i = 0; i < garden.length; i++) {
                for (int j = 0; j < garden[i].length; j++) {
                    if (garden[i][j] == 0) {
                        System.out.print("W ");
                    } else {
                        System.out.print("H ");
                    }
                }
                System.out.println();
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

