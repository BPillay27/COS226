
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // done
        final int numThreads=4;
        ButterCroissant stock=new ButterCroissant(numThreads);

        BakeryWorkerThread[]workers=new BakeryWorkerThread[numThreads];

        for(int i=0;i<numThreads;i++){
            workers[i]=new BakeryWorkerThread(i, stock);
            workers[i].setName("Worker-"+i);
            workers[i].start();
        }

        for(BakeryWorkerThread w : workers){
            w.join();
        }

        int total=stock.getTotal();
        int expected=numThreads * 5;

        System.out.println("Total croissants baked: "+ total);
        System.out.println("Expected: "+ expected);
    }
}
