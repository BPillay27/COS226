import java.util.*;
import java.util.concurrent.*;

public class Task3 {
    public static final int POISON_PILL = -1;
    public static final int PIZZAS_PER_PRODUCER = 10;

    static class Producer implements Callable<List<Integer>> {
        private final int id;
        private final BlockingQueue<Integer> queue;

        public Producer(int id, BlockingQueue<Integer> queue) {
            this.id = id;
            this.queue = queue;
        }

        public List<Integer> call() throws Exception {
            // done
            List<Integer> produced = new ArrayList<>();
            Random rand = new Random();

            for (int i = 0; i < PIZZAS_PER_PRODUCER; i++) {
                int pizza = i;
                queue.put(pizza);
                produced.add(pizza);
                Thread.sleep(rand.nextInt(300) + 100);
            }
            //queue.put(POISON_PILL);
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

        public List<Integer> call() throws Exception {
            // done
            List<Integer> delivered = new ArrayList<>();
            Random rand = new Random();

            while (true) {
                int item = queue.take();

                if (item == POISON_PILL) {
                    break;
                }
                delivered.add(item);
                Thread.sleep(rand.nextInt(400) + 100);
            }
            return delivered;
        }
    }
}