import java.util.*;
import java.util.concurrent.*;

public class Task3 {
    public static final int POISON_PILL = -1;
    private static final int PIZZAS_PER_PRODUCER=5;

    static class Producer implements Callable<List<Integer>> {
        private final int id;
        private final BlockingQueue<Integer> queue;

        public Producer(int id, BlockingQueue<Integer> queue) {
            this.id = id;
            this.queue = queue;
        }

        public List<Integer> call() throws Exception {
            //done
            List<Integer> made=new ArrayList<>(PIZZAS_PER_PRODUCER);
            ThreadLocalRandom rnd=ThreadLocalRandom.current();

            for(int i=0;i<PIZZAS_PER_PRODUCER;i++){
                Thread.sleep(rnd.nextInt(50,151));
                int pizza=id*1000+i;
                queue.put(pizza);
                made.add(pizza);
            }
            return made;
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
            //done
            List<Integer> delivered=new ArrayList<>();
            ThreadLocalRandom rnd=ThreadLocalRandom.current();

            while(true){
                int x=queue.take();
                if(x==POISON_PILL){
                    queue.put(POISON_PILL);
                    break;
                }
                Thread.sleep(rnd.nextInt(80,201));
                delivered.add(x);
            }
            return delivered;
        }
    }
}