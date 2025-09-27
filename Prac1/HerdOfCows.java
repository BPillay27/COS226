
public class HerdOfCows {
    private int cowNum = 0;
    private final PattersonLock lock;

    public HerdOfCows(int threadNum) {
        this.lock = new PattersonLock(threadNum);
    }

    public int getCowAmount() {
        return cowNum;
    }

    public int countCows(int threadId) {
        // done
        lock.lock(threadId);

        try {
            cowNum++;
            System.out.println("Thread-" + threadId + " (ID " + threadId + ") has incremented to " + cowNum);
            return cowNum;
        } finally {
            lock.unlock(threadId);
        }

    }
}
