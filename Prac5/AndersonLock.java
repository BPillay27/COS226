import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class AndersonLock {
    private final AtomicBoolean[] slots; // These are my flags.
    private final int size; // num Threads
    private final AtomicInteger tail = new AtomicInteger(0);
    private final ThreadLocal<Integer> localSlot = ThreadLocal.withInitial(() -> 0);

    public AndersonLock(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("capacity must be > 0");
        // done

        size = capacity;
        slots = new AtomicBoolean[size];

        for (int i = 0; i < size; i++) {
            slots[i] = new AtomicBoolean(false);
        }

        slots[0].set(true);
    }

    public void lock() {
        // done
        final int my = Math.floorMod(tail.getAndIncrement(), size);
        localSlot.set(my);

        while (!slots[my].get()) {
            Thread.yield();
        }
    }

    public void unlock() {
        // done
        final int my = localSlot.get();
        slots[my].set(false);
        slots[(my + 1) % size].set(true);
    }
}
