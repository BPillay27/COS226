import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        FeedingQueue q = new FeedingQueue();

        // ---- 1) Single-thread sanity ----
        System.out.println("== Single-thread sanity ==");
        System.out.println("Add 3, 1, 2 (unsorted input)...");
        System.out.println("add(3) => " + q.addAnimal(3));
        System.out.println("add(1) => " + q.addAnimal(1));
        System.out.println("add(2) => " + q.addAnimal(2));
        System.out.println("add(2) again (dup) => " + q.addAnimal(2));

        System.out.println("contains(1) = " + q.isAnimalInQueue(1));
        System.out.println("contains(4) = " + q.isAnimalInQueue(4));

        System.out.println("snapshot = " + q.getQueueSnapshot());
        assertSortedNoDups(q.getQueueSnapshot());

        System.out.println("feed(2) => " + q.feedAnimal(2));
        System.out.println("feed(4) (absent) => " + q.feedAnimal(4));
        System.out.println("snapshot = " + q.getQueueSnapshot());
        assertSortedNoDups(q.getQueueSnapshot());

        // ---- 2) Multithread add + contains ----
        System.out.println("\n== Multithread add + contains ==");
        final int THREADS = 8;
        final int N = 50; // animal IDs 1..N
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);

        // Add many (with duplicates) concurrently
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int t = 0; t < THREADS; t++) {
            final int tid = t;
            tasks.add(() -> {
                for (int i = 1; i <= N; i++) {
                    // add duplicates intentionally
                    q.addAnimal(i);
                    if ((i + tid) % 7 == 0) q.addAnimal(i);
                }
                return null;
            });
        }
        pool.invokeAll(tasks);

        // Quick membership checks (concurrent reads)
        List<Callable<Void>> containsTasks = new ArrayList<>();
        for (int t = 0; t < THREADS; t++) {
            containsTasks.add(() -> {
                for (int i = 1; i <= N; i++) {
                    if (!q.isAnimalInQueue(i)) {
                        throw new IllegalStateException("Missing ID after adds: " + i);
                    }
                }
                return null;
            });
        }
        pool.invokeAll(containsTasks);

        List<Integer> snapAfterAdds = q.getQueueSnapshot();
        System.out.println("snapshot after concurrent adds (size=" + snapAfterAdds.size() + "): " + snapAfterAdds);
        assertSortedNoDups(snapAfterAdds);
        expectRange(snapAfterAdds, 1, N);

        // ---- 3) Multithread feeds (removes) ----
        System.out.println("\n== Multithread feeds (removes) ==");
        AtomicInteger removed = new AtomicInteger(0);
        List<Callable<Void>> feedTasks = new ArrayList<>();
        // Remove even IDs concurrently
        for (int t = 0; t < THREADS; t++) {
            feedTasks.add(() -> {
                for (int i = 2; i <= N; i += 2) {
                    if (q.feedAnimal(i)) removed.incrementAndGet();
                }
                return null;
            });
        }
        pool.invokeAll(feedTasks);

        List<Integer> snapAfterFeeds = q.getQueueSnapshot();
        System.out.println("removed count (even IDs) = " + removed.get());
        System.out.println("snapshot after feeds (size=" + snapAfterFeeds.size() + "): " + snapAfterFeeds);
        assertSortedNoDups(snapAfterFeeds);

        // Expect only odd IDs remaining
        for (int i = 1; i <= N; i++) {
            boolean present = q.isAnimalInQueue(i);
            if (i % 2 == 0 && present) {
                throw new IllegalStateException("Even ID still present: " + i);
            }
            if (i % 2 == 1 && !present) {
                throw new IllegalStateException("Odd ID missing: " + i);
            }
        }

        pool.shutdown();
        System.out.println("\nAll tests passed!");
    }

    // --- Helpers ---

    private static void assertSortedNoDups(List<Integer> list) {
        int prev = Integer.MIN_VALUE;
        for (int v : list) {
            if (v <= prev) {
                throw new IllegalStateException("List not strictly sorted or has dups at value " + v +
                        " sequence=" + list);
            }
            prev = v;
        }
    }

    private static void expectRange(List<Integer> list, int lo, int hi) {
        // Expect every value in [lo..hi] appears (once)
        if (list.size() != (hi - lo + 1)) {
            throw new IllegalStateException("Unexpected size; expected " + (hi - lo + 1) + " got " + list.size());
        }
        for (int i = lo; i <= hi; i++) {
            if (Collections.binarySearch(list, i) < 0) {
                throw new IllegalStateException("Missing value " + i);
            }
        }
    }
}
