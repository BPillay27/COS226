import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TTASLock implements Lock {
    private final AtomicBoolean state = new AtomicBoolean(false);
    private static final int MIN_DELAY = 10; // in microseconds
    private static final int MAX_DELAY = 1000; // max backoff delay (µs)

    // Counter to track how many times backoff (sleep) occurred
    public final AtomicInteger backoffCount = new AtomicInteger(0);

    public void lock() {
        // done
        int currDelay=MIN_DELAY;

        while(true){
            while(state.get()){
                    
            }
            if(!state.getAndSet(true)){
                return;
            }
            else{
                try{
                    backoffCount.incrementAndGet();
                    int _sleep=ThreadLocalRandom.current().nextInt(currDelay);
                    Thread.sleep(0,_sleep*1000);
                    currDelay=Math.min(MAX_DELAY,2*currDelay);
                } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void unlock() {
        // done
        state.set(false);
    }
}
