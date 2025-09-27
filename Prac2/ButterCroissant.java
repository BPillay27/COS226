
public class ButterCroissant {
    private int count = 0;
    private final int maxThreads;
    private final BakeryLock lock;
    //Add Lock here

    public ButterCroissant(int t) {
        this.count = 0;
        this.maxThreads = t;
        //Initalize lock here
        this.lock=new BakeryLock(maxThreads);
    }

    public int bake(int threadId) {
        //done
        lock.lock(threadId);

        try{
            count++;
            return count;
        } finally{
            lock.unlock(threadId);
        }
    }

    public int getTotal() {
        return count;
    }
}
