//import java.util.*;
public class Task1 {
    static class ReusableBarrier {
        private final int friends;
        private final Runnable task;

        private int completed = 0;
        private volatile int current_pizza = 0;

        // add lock here
        private final CLHLock lock=new CLHLock();
        public ReusableBarrier(int friends) {
            this(friends, null);
        }

        public ReusableBarrier(int friends, Runnable task) {
            if (friends <= 0)
                throw new IllegalArgumentException("friends must be > 0");
            this.friends = friends;
            this.task = task;
        }

        public void await() throws InterruptedException {
            // done
            final int myGen;
            lock.lock();
            try {
                myGen = current_pizza;
                int arrivals = ++completed;

                if (arrivals == friends) {
                    try {
                        if (task != null)
                            task.run();
                    } finally {
                        completed = 0;
                        current_pizza=myGen+1;
                    }
                    return;
                } 
            } finally {
                lock.unlock();
            }

            while (current_pizza==myGen) {
                if(Thread.interrupted()){
                    throw new InterruptedException();
                }
                Thread.onSpinWait();
            }
        }
    }
}
