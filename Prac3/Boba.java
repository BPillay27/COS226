
public class Boba extends Thread {
	private final int threadId;
	private final Straw bobaStraw;

	Boba(Straw s, int threadId) {
		// done
		this.bobaStraw = s;
		this.threadId = threadId;
	}

	@Override
	public void run() {
		// done
		try {
			System.out.println("Boba " + threadId + " waiting patiently to be sipped");
			for (int i = 0; i < 3; i++) {
				int x = (int) (Math.random() * 100);
				Thread.sleep(x);

				bobaStraw.sip(threadId);
			}
		} catch (InterruptedException e) {
			// done
			Thread.currentThread().interrupt();
		}

	}
}
