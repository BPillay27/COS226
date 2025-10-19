public class Task1 {
    static class ReusableBarrier {
        private final int friends;
        private final Runnable task;

        private int completed = 0;
        private int current_pizza = 0;

        // add lock here
        private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();
        private final java.util.concurrent.locks.Condition con = lock.newCondition();

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
                int myPizza = current_pizza;
                completed++;

                if (completed == friends) {
                    if (task != null)
                        task.run();
                    completed = 0;
                    current_pizza++;
                    con.signalAll();
                } else {
                    while (myPizza == current_pizza) {
                        con.await();
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
