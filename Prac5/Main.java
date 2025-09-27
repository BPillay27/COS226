public class Main {
    public static void main(String[] args) {
        // done
        final int numShips = 4;
        final int dockings = 3;
        final AndersonLock lock = new AndersonLock(numShips);

        SpaceShip[] fleet = new SpaceShip[numShips];
        for (int i = 0; i < numShips; i++) {
            fleet[i] = new SpaceShip(i, lock, dockings);
        }

        for (SpaceShip s : fleet) {
            s.start();
        }

        for (SpaceShip s : fleet) {
            try {
                s.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
