import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== TASK 1: ReusableBarrier ===");
        testTask1Barrier();

        System.out.println("\n=== TASK 2: Coarse/Fine Blocking Queues ===");
        testTask2Queues();

        System.out.println("\n=== TASK 3: Producers/Consumers (10 pizzas per producer) ===");
        testTask3ProducersConsumers();
    }

    private static void testTask1Barrier() throws Exception {
        final int friends = 4;
        final int rounds = 3;

        Task1.ReusableBarrier barrier = new Task1.ReusableBarrier(friends,
                () -> System.out.println("Barrier action: all friends arrived for this pizza round!"));

        ExecutorService pool = Executors.newFixedThreadPool(friends);
        List<Future<?>> fs = new ArrayList<>();

        for (int i = 0; i < friends; i++) {
            final int id = i;
            fs.add(pool.submit(() -> {
                ThreadLocalRandom rnd = ThreadLocalRandom.current();
                for (int r = 1; r <= rounds; r++) {
                    try {
                        // simulate arriving at different times
                        Thread.sleep(rnd.nextInt(50, 200));
                        System.out.println("Friend " + id + " reached barrier (round " + r + ")");
                        barrier.await();
                        System.out.println("Friend " + id + " passed barrier (round " + r + ")");
                    } catch (InterruptedException e) {
                        System.out.println("Interrupt occured");
                    }
                }
            }));
        }

        for (Future<?> f : fs)
            f.get();
        pool.shutdownNow();
        System.out.println("Task1 barrier test complete.");
    }

    private static void testTask2Queues() throws Exception {
        final int N = 20;
        final int CAP = 5;

        // Coarse-grained queue test
        Task2.MyBlockingQueue<Integer> coarse = new Task2.CoarseGrainedBlockingQueue<>(CAP);
        runQueueThroughputTest("Coarse", coarse, N);

        // Fine-grained queue test
        Task2.MyBlockingQueue<Integer> fine = new Task2.FineGrainedBlockingQueue<>(CAP);
        runQueueThroughputTest("Fine  ", fine, N);
    }

    private static void runQueueThroughputTest(String label,
            Task2.MyBlockingQueue<Integer> q,
            int n) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<List<Integer>> prod = pool.submit(() -> {
            List<Integer> made = new ArrayList<>();
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            for (int i = 0; i < n; i++) {
                q.enqueue(i);
                made.add(i);
                Thread.sleep(rnd.nextInt(5, 15));
            }
            return made;
        });

        Future<List<Integer>> cons = pool.submit(() -> {
            List<Integer> got = new ArrayList<>();
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            for (int i = 0; i < n; i++) {
                got.add(q.dequeue());
                Thread.sleep(rnd.nextInt(5, 15));
            }
            return got;
        });

        List<Integer> produced = prod.get();
        List<Integer> consumed = cons.get();
        pool.shutdownNow();

        System.out.println(label + " | produced=" + produced.size()
                + ", consumed=" + consumed.size()
                + ", final size=" + q.size());
    }

    private static void testTask3ProducersConsumers() throws Exception {
        final int producers = 2;
        final int consumers = 3;
        final int capacity = 5;

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(capacity, true);
        ExecutorService pool = Executors.newFixedThreadPool(producers + consumers);
        List<Future<List<Integer>>> futures = new ArrayList<>();

        for (int i = 1; i <= producers; i++) {
            futures.add(pool.submit(new Task3.Producer(i, queue)));
        }

        for (int i = 1; i <= consumers; i++) {
            futures.add(pool.submit(new Task3.Consumer(i, queue)));
        }

        for (int i = 0; i < producers; i++) {
            futures.get(i).get();
        }

        for (int i = 0; i < consumers; i++) {
            queue.put(Task3.POISON_PILL);
        }

        for (int i = producers; i < producers + consumers; i++) {
            futures.get(i).get();
        }

        pool.shutdownNow();

        int expectedPizzas = producers * Task3.PIZZAS_PER_PRODUCER;
        System.out.println("Task3 complete. Expected pizzas: " + expectedPizzas);
    }
}
