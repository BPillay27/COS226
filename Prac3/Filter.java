public class Filter {
        final int threadNum;
        final int[] level;
        volatile int[] vic;

        public Filter(int num_of_threads) {
                // doen

                this.threadNum = num_of_threads;
                this.level = new int[threadNum];
                this.vic = new int[threadNum - 1];

                for (int i = 0; i < level.length; i++) {
                        level[i] = -1;
                }
        }

        public void lock(int threadId) {
                // done
                for(int i=0;i<threadNum-1;i++){
                        level[threadId]=i;
                        vic[i]=threadId;

                        boolean blocked;

                        do{
                             blocked=false;
                             for(int k=0;k<threadNum;k++){
                                if(k==threadId) continue;
                                if(level[k]>=i && vic[i]==threadId){
                                        blocked=true;
                                        break;
                                }
                             }   
                        }while (blocked);
                }
        }

        public void unlock(int threadId) {
                // done
                level[threadId]=-1;
        }
}
