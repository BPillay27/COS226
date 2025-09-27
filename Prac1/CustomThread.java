
public class CustomThread extends Thread {
    private final FlockOfSheep baa;
    private final HerdOfCows moo;
    private final int threadId;
    private final int max_count = 100;

    public CustomThread(FlockOfSheep baa, int threadId) {
        this.baa = baa;
        this.moo = null;
        this.threadId = threadId;
    }

    public CustomThread(HerdOfCows moo, int threadId) {
        this.moo = moo;
        this.baa = null;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        // Done
        if (baa != null)
            releaseTheSheep();

        if (moo != null)
            releaseTheCows();
    }

    public void releaseTheSheep() {
        // done
        int k = 0;
        System.out.println("Thread-" + threadId + " (ID " + threadId + ") is running");
        while (k != max_count) {
            baa.countSheep(threadId);
            k++;
        }
    }

    public void releaseTheCows() {
        // done
        int k = 0;
        System.out.println("Thread-" + threadId + " (ID " + threadId + ") is running");
        while (k != max_count) {
            moo.countCows(threadId);
            k++;
        }

    }

}
