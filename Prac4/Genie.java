
public class Genie extends Thread {
    private ConsensusProtocol protocol;
    private final int threadId;
    private int value;
    private int decision;

    public Genie(ConsensusProtocol protocol, int id, int value) {
        this.protocol = protocol;
        this.threadId = id;
        this.value = value;
        this.decision = -1;
    }

    public void run() {
        // done
        try {
            int x = (int) (Math.random() * 100);
            Thread.sleep(x);

            decision = protocol.decide(threadId, value);

        } catch (InterruptedException exception) {
            System.out.println("Genie interrupted while sleeping.");
        } catch (Exception e) {
            System.out.println("Genie failed.");
            decision = -1; // default value for failure
        }
    }

    public int getDecision() {
        // done
        return decision;
    }

}
