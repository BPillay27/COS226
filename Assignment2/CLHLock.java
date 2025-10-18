public class CLHLock {
    private static final class QNode {
        volatile boolean locked;
    }

    private static final Object ENQ = new Object();
    private QNode tail = new QNode();
    private final ThreadLocal<QNode> myNode = ThreadLocal.withInitial(QNode::new);
    private final ThreadLocal<QNode> myPred = new ThreadLocal<>();

    public void lock(){
        QNode node=myNode.get();
        node.locked=true;

        QNode pred;
        synchronized (ENQ){
            pred=tail;
            tail=node;
        }
        myPred.set(pred);

        while (pred.locked) {
            Thread.onSpinWait(); 
        }
    }

    public void unlock(){
        QNode node =myNode.get();
        node.locked=false;
        myNode.set(myPred.get());
    }
}
