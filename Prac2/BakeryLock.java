public class BakeryLock {
    private final ThreadLocal<Boolean> isLocked = ThreadLocal.withInitial(() -> false);
    private final int n;
    private final boolean[] choosing;
    private final int[] number;

    public BakeryLock(int input) {
        // done
        this.n = input;
        this.choosing = new boolean[n];
        this.number = new int[n];
        for (int i = 0; i < n; i++) {
            choosing[i] = false;
            number[i] = 0;
        }

    }

    public void lock(int threadId) {
        // done
        choosing[threadId]=true;
        int max=0;
        for(int i=0;i<n;i++){
            if(number[i]>max){
                max=number[i];
            }
        }

        number[threadId]=max+1;
        choosing[threadId]=false;

        for(int j=0;j<n;j++){
            if(j==threadId) continue;

            while(choosing[j]) {Thread.yield();}

            while (number[j]!=0 && (number[j]<number[threadId] || (number[j]==number[threadId]&&j<threadId))) {
                Thread.yield();
            }
        }
        isLocked.set(true);
    }

    public void unlock(int threadId){
        isLocked.set(false);
        number[threadId]=0;
    }
}
