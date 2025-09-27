import java.util.Vector;

public abstract class ConsensusProtocol {

	private int threadCount;
	protected Vector<Integer> proposed;

	public ConsensusProtocol(int threadCount) {
		// done
		this.threadCount = threadCount;
		this.proposed = new Vector<>(threadCount);

		for (int i = 0; i < threadCount; i++) {
			proposed.add(null);
		}
	}

	void propose(int threadId, int value) {
		// done
		if (threadId < 0 || threadId >= threadCount) {
			throw new IllegalArgumentException("Invalid threadId");
		}
		proposed.set(threadId, value);
	}

	public abstract int decide(int id, int value);
}


