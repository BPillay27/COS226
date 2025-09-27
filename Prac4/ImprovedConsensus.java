
import java.util.LinkedList;
import java.util.Queue;

public class ImprovedConsensus extends ConsensusProtocol {
    private Queue<Integer> queue = new LinkedList<>();
    private final int n;
    private final int[] level;
    private final int[] victim;

    private volatile boolean decided = false;

    public ImprovedConsensus(int threadCount) {
        // done
        super(threadCount);
        this.n = threadCount;
        this.level = new int[n];
        this.victim = new int[n];
    }

    @Override
    public int decide(int threadId, int value) {
        // done
        propose(threadId, value);

        lock(threadId);

        try {
            if (!decided) {
                queue.offer(value);
                decided = true;
            }

            return queue.peek();
        } finally {
            unlock(threadId);
        }
    }

    private void lock(int i) {
        for (int L = 1; L < n; L++) {
            level[i] = L;
            victim[L] = i;

            while (victim[L] == i && existsAtOrAbove(L, i)) {
                Thread.yield();
            }
        }
    }

    private void unlock(int i) {
        level[i] = 0;
    }

    private boolean existsAtOrAbove(int L, int me) {
        for (int k = 0; k < n; k++) {
            if (k == me)
                continue;
            if (level[k] >= L)
                return true;
        }

        return false;
    }

}
