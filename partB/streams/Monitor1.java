package partB.streams;

import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.io.FileWriter;
import java.io.IOException;

public class Monitor1 implements Runnable {

    private int[][] garden;
    private ReadWriteLock lock;

    public Monitor1(int[][] garden, ReadWriteLock lock) {
        this.garden = garden;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            lock.readLock().lock();

            FileWriter writer = null;
            try {
                writer = new FileWriter("garden_state.txt", true);

                writer.write("Garden state at time " + new Date() + ":\n");
                for (int i = 0; i < garden.length; i++) {
                    for (int j = 0; j < garden[i].length; j++) {
                        writer.write(garden[i][j] + " ");
                    }
                    writer.write("\n");
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
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

