
public class PetersonLock {
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public void lock(int i) {
        // Done
        // victim is most likely the turn variable
        if (i != 0 && i != 1) {
            return;
        }
        int j = 1 - i;

        flag[i] = true;
        victim = i;

        while (flag[j] && victim == i) {

        }
    }

    public void unlock(int i) {
        // Done
        flag[i] = false;
        return;
    }
}
