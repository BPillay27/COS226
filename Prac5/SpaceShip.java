public class SpaceShip extends Thread {
    private final int threadId;
    private final AndersonLock lock;
    private final int dockings;

    public SpaceShip(int id, AndersonLock lock, int dockings) {
        // done
        this.threadId = id;
        this.lock = lock;
        this.dockings = dockings;
    }

    private void dock() {
        // done
        lock.lock();
        try {
            System.out.println("Ship " + threadId + " is DOCKING");
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Ship " + threadId + " has LEFT the docking port");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        // done
        for (int i = 0; i < dockings; i++) {
            dock();
            try {
                Thread.sleep((int) (Math.random() * 300));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public int getID() {
        return threadId;
    }
}