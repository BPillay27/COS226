import java.util.*;


public class Main {

    enum Contention {
        LOW(thinkMs(15), work(200)),
        MEDIUM(thinkMs(5), work(800)),
        HIGH(thinkMs(0), work(2500));

        final int thinkMs;
        final int csWorkIters;

        Contention(int thinkMs, int csWorkIters) {
            this.thinkMs = thinkMs;
            this.csWorkIters = csWorkIters;
        }

        static int thinkMs(int v) {
            return v;
        }

        static int work(int v) {
            return v;
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            String contention = (args.length > 0) ? args[0] : "LOW";
        int _players = (args.length > 1) ? Integer.parseInt(args[1]) : 2;
        String lockType = (args.length > 2) ? args[2] : "CLH";
        int chestCount = (args.length > 3) ? Integer.parseInt(args[3]) : 2;
            
            
            System.out.printf("Args => contention=%s players=%d lock=%s chests=%d%n",
                contention, _players, lockType, chestCount);
            validateArgs(args);

            Contention level = Contention.valueOf(args[0].toUpperCase());
            int numPlayers = Math.abs(Integer.parseInt(args[1]));
            Lock _lock = (args[2].toUpperCase().equals("CLH")) ? new CLHLock() : new TTASLock();
            int numChest = Math.abs(Integer.parseInt(args[3]));

            List<Player> players = new ArrayList<>();
            List<TreasureChest> chests = new ArrayList<>();

            for (int i = 0; i < numChest; i++) {
                Lock _temp=(args[2].toUpperCase().equals("CLH")) ? new CLHLock() : new TTASLock();
                chests.add(new TreasureChest("Chest " + i, 2000, _temp));
            }

            for (int i = 0; i < numPlayers; i++) {
                players.add(new Player("Player " + i, chests, level.thinkMs, level.csWorkIters));
                players.get(i).start();
            }

            for (Player p : players) {
                p.join();
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Arguement exception: " + e);
            return;
        }

    }

    public static void validateArgs(String[] input) {
        if (input.length != 4) {
            throw new IllegalArgumentException(
                    "Too few/many Arguments. Arguments must be [ContentionType] [no. of players] [Type of Lock] [no. of chests].");
        }

        String temp = input[0].toUpperCase();
        if (!temp.equals("HIGH") && !temp.equals("MEDIUM") && !temp.equals("LOW")) {
            throw new IllegalArgumentException("Invalid contention. Must be LOW, MEDIUM or HIGH.");
        }

        if (!isInteger(input[1])) {
            throw new IllegalArgumentException("Invalid number of players. Must be a number.");
        }

        int i = Integer.parseInt(input[1]);
        if (i < 1) {
            throw new IllegalArgumentException("Invalid number of players. Must greater than or equal 1.");
        }

        temp = input[2].toUpperCase();
        if (!temp.equals("TTAS") && !temp.equals("CLH")) {
            throw new IllegalArgumentException("Invalid lock type. Must be CLH or TTAS.");
        }

        if (!isInteger(input[3])) {
            throw new IllegalArgumentException("Invalid number of chests. Must be a number.");
        }

        i = Integer.parseInt(input[3]);
        if (i < 1) {
            throw new IllegalArgumentException("Invalid number of chests. Must greater than or equal 1.");
        }
    }

    public static boolean isInteger(String input) {
        if (input == null)
            return false;

        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
