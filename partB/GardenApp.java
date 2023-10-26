package partB;

import partB.streams.Gardener;
import partB.streams.Nature;
import partB.streams.Monitor1;
import partB.streams.Monitor2;


import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GardenApp {

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private static int[][] garden;

    public static void main(String[] args) {
        garden = new int[10][10];

        Thread gardenerThread = new Thread(new Gardener(garden, lock));
        Thread natureThread = new Thread(new Nature(garden, lock));
        Thread monitor1Thread = new Thread(new Monitor1(garden, lock));
        Thread monitor2Thread = new Thread(new Monitor2(garden, lock));

        gardenerThread.start();
        natureThread.start();
        monitor1Thread.start();
        monitor2Thread.start();
    }
}
