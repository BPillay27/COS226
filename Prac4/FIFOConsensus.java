import java.util.LinkedList;
import java.util.Queue;

public class FIFOConsensus extends ConsensusProtocol {
    private Queue<Integer> queue = new LinkedList<>();
    private final boolean[] want = new boolean[2];
    private volatile int turn = 0;
    private volatile boolean decided = false;
    private volatile int chosen;

    public FIFOConsensus(int threadCount) {
        // done
        super(threadCount);
        // if (threadCount != 2) {
        //     throw new IllegalArgumentException("Invalid threadCount");
        // }
        want[0] = false;
        want[1] = true;
        turn = 0;
    }

    @Override
    public int decide(int threadId, int value) {
        // done
        // if (threadId < 0 || threadId > 1) {
        //     throw new IllegalArgumentException("ThreadId must be 1 or 0");
        // }
        propose(threadId, value);

        lock(threadId);
        try {
            if (!decided) {
                queue.offer(value);
                chosen = value;
                decided = true;
            }
            return chosen;
        } finally {
            unlock(threadId);
        }

    }

    private void lock(int i) {
        int j = 1 - i;
        want[i] = true;
        turn = j;
        while (want[j] && turn == j) {
            Thread.yield();
        }
    }

    private void unlock(int i) {
        want[i] = false;
    }
}
