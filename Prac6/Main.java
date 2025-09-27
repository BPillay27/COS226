import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // done
        int K = 3; // capacity
        int N = 10; // num visitors

        SemaphoreLock lock = new SemaphoreLock(K);
        List<Integer> boardingLog = Collections.synchronizedList(new ArrayList<>());

        List<Thread> visitors = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            Thread t = new Thread(new Visitor(i, lock, boardingLog));
            visitors.add(t);
            t.start();
        }

        for (Thread t : visitors) {
            t.join();
        }

        System.out.println("Boarding Log:\n");
        synchronized (boardingLog) {
            for (int id : boardingLog) {
                System.out.println("Visitor " + id + " boarded.");
            }
        }
    }
}
