import java.util.concurrent.atomic.AtomicInteger;

public class Task2 {
    public interface MyBlockingQueue<T> {
        void enqueue(T item) throws InterruptedException;

        T dequeue() throws InterruptedException;

        int size();

        int capacity();
    }

    public static class CoarseGrainedBlockingQueue<T> implements MyBlockingQueue<T> {
        private final Object[] queue;
        private int head = 0, tail = 0, count = 0;

        // add any lock and conditions here
        private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();
        private final java.util.concurrent.locks.Condition notFull = lock.newCondition();
        private final java.util.concurrent.locks.Condition notEmpty = lock.newCondition();

        public CoarseGrainedBlockingQueue(int capacity) {
            if (capacity <= 0)
                throw new IllegalArgumentException("capacity must be > 0");
            this.queue = new Object[capacity];
        }

        @Override
        public void enqueue(T item) throws InterruptedException {
            // done
            lock.lock();
            try {
                while (count == queue.length) {
                    notFull.await();
                }
                queue[tail] = item;
                tail = (tail + 1) % queue.length;
                count++;
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T dequeue() throws InterruptedException {
            // done
            lock.lock();
            try {
                while (count == 0) {
                    notEmpty.await();
                }
                Object x = queue[head];
                queue[head] = null;
                head = (head + 1) % queue.length;
                count--;
                notFull.signal();
                return (T) x;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int size() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int capacity() {
            return queue.length;
        }
    }

    public static class FineGrainedBlockingQueue<T> implements MyBlockingQueue<T> {
        private final Object[] queue;
        private volatile int head = 0, tail = 0;
        private final AtomicInteger size = new AtomicInteger(0);

        // add any lock and conditions here
        private final java.util.concurrent.locks.Lock enqLock = new java.util.concurrent.locks.ReentrantLock();
        private final java.util.concurrent.locks.Lock deqLock = new java.util.concurrent.locks.ReentrantLock();

        private final java.util.concurrent.locks.Condition notFull = enqLock.newCondition();
        private final java.util.concurrent.locks.Condition notEmpty = deqLock.newCondition();

        public FineGrainedBlockingQueue(int capacity) {
            if (capacity <= 0)
                throw new IllegalArgumentException("capacity must be > 0");
            this.queue = new Object[capacity];
        }

        @Override
        public void enqueue(T item) throws InterruptedException {
            // done
            enqLock.lock();
            try {
                while (size.get() == queue.length) {
                    notFull.await();
                }
                queue[tail] = item;
                tail = (tail + 1) % queue.length;
                int newSize = size.incrementAndGet();

                if (newSize == 1) {
                    deqLock.lock();
                    try {
                        notEmpty.signal();
                    } finally {
                        deqLock.unlock();
                    }
                }
            } finally {
                enqLock.unlock();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T dequeue() throws InterruptedException {
            // done
            deqLock.lock();
            try {
                while (size.get() == 0) {
                    notEmpty.await();
                }
                Object x = queue[head];
                queue[head] = null;
                head = (head + 1) % queue.length;
                int newSize = size.decrementAndGet();

                if (newSize == queue.length - 1) {
                    enqLock.lock();
                    try {
                        notFull.signal();
                    } finally {
                        enqLock.unlock();
                    }
                }
                return (T) x;
            } finally {
                deqLock.unlock();
            }
        }

        @Override
        public int size() {
            return size.get();
        }

        @Override
        public int capacity() {
            return queue.length;
        }
    }
}