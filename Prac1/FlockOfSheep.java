
public class FlockOfSheep {
    private int sheepNum = 0;
    private final PetersonLock lock = new PetersonLock();
    private volatile int turn = 0;

    public int getSheepAmount() {
        return sheepNum;
    }

    public int countSheep(int threadId) {
        // Done
        if (threadId != 1 && threadId != 0) {
            return -1;
        }

        while (turn != threadId) {
            Thread.yield();
        }

        lock.lock(threadId);
        ++sheepNum;
        System.out.println("Thread-" + threadId + " (ID " + threadId + ") has incremented to " + sheepNum);
        lock.unlock(threadId);

        turn = 1 - threadId;

        return sheepNum;
    }
}
