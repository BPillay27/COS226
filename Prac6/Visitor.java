import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class Visitor implements Runnable {
    private final int id;
    private final SemaphoreLock seats;
    private final List<Integer> boardingLog;

    public Visitor(int id, SemaphoreLock seats, List<Integer> boardingLog) {
        this.id = id;
        this.seats = seats;
        this.boardingLog = boardingLog;
    }

    @Override
    public void run() {
        // done
        seats.lock(this);
        try {
            System.out.println("Visitor " + id + " arrives at the ride.");
            synchronized (boardingLog) {
                boardingLog.add(id);
            }

            try {
                System.out.println("Visitor " + id + " boards the ride!");
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Visitor " + id + " leaves the ride.");
        } finally {
            seats.unlock();
        }
    }

    public String toString() {
        return "Visitor " + id;
    }
}
