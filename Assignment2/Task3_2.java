import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Task3_2 {

    public static final int PIZZAS_PER_PRODUCER = 10;
    private static final AtomicInteger producersRemaining = new AtomicInteger(0);

    static class Producer implements Callable<List<Integer>> {
        private final int id;
        private final BlockingQueue<Integer> queue;

        public Producer(int id, BlockingQueue<Integer> queue) {
            this.id = id;
            this.queue = queue;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> produced = new ArrayList<>();
            Random rand = new Random();

            for (int i = 0; i < PIZZAS_PER_PRODUCER; i++) {
                queue.put(i); // produce pizza
                produced.add(i);
                Thread.sleep(rand.nextInt(300) + 100);
            }

            producersRemaining.decrementAndGet(); // signal producer finished
            return produced;
        }
    }

    static class Consumer implements Callable<List<Integer>> {
        private final int id;
        private final BlockingQueue<Integer> queue;

        public Consumer(int id, BlockingQueue<Integer> queue) {
            this.id = id;
            this.queue = queue;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> delivered = new ArrayList<>();
            Random rand = new Random();

            while (true) {
                Integer pizza = queue.poll(300, TimeUnit.MILLISECONDS);

                if (pizza != null) {
                    delivered.add(pizza);
                    Thread.sleep(rand.nextInt(400) + 100);
                } else if (producersRemaining.get() == 0 && queue.isEmpty()) {
                    break; // No producers left AND no pizzas → safe to stop
                }
            }
            return delivered;
        }
    }

    public static void initProducersRemaining(int n) {
    producersRemaining.set(n);   // OK: the AtomicInteger reference is final, but its value can change
}
}

