import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

// import javax.management.relation.RoleNotFoundException;

public class Main {
    public static void main(String[] args) {
        // done
        testFIFO();
        TestImproved(8);
    }

    private static void testFIFO() {
        try {
            FIFOConsensus proto = new FIFOConsensus(2);

            Genie g0 = new Genie(proto, 0, 10);
            Genie g1 = new Genie(proto, 1, 20);

            g0.start();
            g1.start();

            g0.join();
            g1.join();

            System.out.println("FIFO decisions: g0=" + g0.getDecision() + " ,g1=" + g1.getDecision());
        } catch (InterruptedException e) {
            System.out.println("Interrupt occured in FIFO Test...");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("FIFO Test failed...");
        }
    }

    private static void TestImproved(int n) {
        Random rng = new Random();
        List<Genie> genies = new LinkedList<>();

        try {
            ImprovedConsensus proto = new ImprovedConsensus(n);

            for (int id = 0; id < n; id++) {
                int val = rng.nextInt(100);
                genies.add(new Genie(proto, id, val));
            }

            for (Genie g : genies)
                g.start();

            ListIterator<Genie> it = genies.listIterator();
            while (it.hasNext()) {
                it.next().join();
            }

        } catch (InterruptedException e) {
            System.out.println("Interrupt in ImprovedConsensus testing...");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Error in ImoprovedConsensus testing...");
        }
    }

}
