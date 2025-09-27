
public class Task1Test {
    public static void main(String[] args) throws InterruptedException {
        // done
        FlockOfSheep sheep = new FlockOfSheep();

        int n = 2;
        CustomThread[] threads = new CustomThread[n];
        threads[0] = new CustomThread(sheep, 0);
        threads[1] = new CustomThread(sheep, 1);

        threads[1].start();
        threads[0].start();

        for (CustomThread t : threads) {
            t.join();
        }

        int x = 100;
        System.out.println("Expected total sheep counted: " + x);
        System.out.println("Final sheep counted: " + sheep.getSheepAmount() / n);

        if (x == sheep.getSheepAmount() / n) {
            System.out.println("Test PASSED.");
        } else {
            System.out.println("Test FAILED.");
        }
    }
}
