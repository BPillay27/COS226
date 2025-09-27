
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreLock {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition con = lock.newCondition();
    private final int capacity;
    private int seats_taken = 0;
    private final Queue<Visitor> waitList = new LinkedList<>();

    public SemaphoreLock(int capacity) {
        // done
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be >0");
        }

        this.capacity = capacity;
    }

    public void lock(Visitor visitor) {
        // done
        if (visitor == null)
            throw new NullPointerException("null visitor");
        lock.lock();
        try {
            waitList.add(visitor);

            while (waitList.peek() != visitor || seats_taken >= capacity) {
                con.awaitUninterruptibly();
            }

            waitList.remove();
            ++seats_taken;
        } finally {
            lock.unlock();
        }
    }

    public void unlock() {
        // done
        lock.lock();
        try {
            if (seats_taken == 0) {
                throw new IllegalStateException("Unlock, but no seats taken");
            }
            --seats_taken;
            con.signalAll(); // can be signal instead
        } finally {
            lock.unlock();
        }
    }
}
