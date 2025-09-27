

public class BakeryWorkerThread extends Thread {
    private final int threadId;
    private final ButterCroissant croissants;
    private final int ovenCapacity = 5;

	public BakeryWorkerThread(int threadId, ButterCroissant croissants) {
        this.threadId = threadId;
        this.croissants = croissants;
    }


 	@Override
    public void run() {
        refillCroissants();
    }

	public void refillCroissants() {
        //done
        for(int i=0;i<ovenCapacity;i++){
            int total=croissants.bake(threadId);
            if(total==-1){
                // can add print here
                break;
            }
            //can add print here
        }
	}
}
