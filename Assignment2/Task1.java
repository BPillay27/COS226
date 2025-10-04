import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Task1 {
    static class ReusableBarrier {
        private final int friends;
        private final Runnable task;

        private int completed = 0;
        private int current_pizza = 0;

        // add lock here
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition con = lock.newCondition();

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
            lock.lock();
            try {
                int myGen = current_pizza;
                int arrivals = ++completed;

                if (arrivals == friends) {
                    try {
                        if (task != null)
                            task.run();
                    } finally {
                        completed = 0;
                        current_pizza++;
                        con.signalAll();
                    }
                } else {
                    while (myGen == current_pizza) {
                        con.await();
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
