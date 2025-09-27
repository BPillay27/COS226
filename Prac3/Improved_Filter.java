
public class Improved_Filter extends Filter {
    // done

    public Improved_Filter(int num_of_threads) {
        // done
        super(num_of_threads);
        if (num_of_threads < 2) {
            throw new IllegalArgumentException("Improved_Filter requires at least 2 threads.");
        }
    }

    @Override
    public void lock(int threadId) {
        // done
        if (threadId < 0 || threadId >= threadNum) {
            throw new IllegalArgumentException("Invalid threadId");

        }

        for (int i = 0; i < threadNum - 1; i++) {
            level[threadId] = i;
            vic[i] = threadId;

            boolean blocked;

            do {
                if (vic[i] != threadId)
                    break;
                blocked = false;
                for (int k = 0; k < threadNum; k++) {
                    if (k == threadId)
                        continue;
                    if (level[k] >= i) {
                        blocked = true;
                        break;
                    }
                }

                if (blocked && vic[i] == threadId) {
                    Thread.yield();
                }
            } while (blocked && vic[i] == threadId);
        }
        // critical section
    }

    @Override
    public void unlock(int threadId) {
        // done
        if (threadId < 0 || threadId >= threadNum) {
            throw new IllegalArgumentException("Invalid threadId");
        }
        level[threadId] = -1;
    }
}
