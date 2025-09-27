
public class Task2Test {
    public static void main(String[] args) throws InterruptedException {
        // done
        int n = 4;
        HerdOfCows cows = new HerdOfCows(n);

        CustomThread[] threads = new CustomThread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new CustomThread(cows, i);
            threads[i].start();
        }

        for (CustomThread t : threads) {
            t.join();
        }

        int expec = 100;

        System.out.println("Expected total cows counted: " + expec);
        int k = cows.getCowAmount() / n;
        System.out.println("Actual total cows counted: " + k);

        if (k == expec) {
            System.out.println("Test PASSED.");
        } else {
            System.out.println("Test Failed");
        }
    }
}
