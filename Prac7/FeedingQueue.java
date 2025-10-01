import java.util.*; // for List, ArrayList

public class FeedingQueue {
    private final Animal head;
    private final Animal tail;

    public FeedingQueue() {
        head = new Animal(Integer.MIN_VALUE);
        tail = new Animal(Integer.MAX_VALUE);
        head.next = tail;
    }

    private boolean validate(Animal pred, Animal curr) {
        // done
        return pred.next == curr && pred.animal_id < curr.animal_id;
    }

    public boolean isAnimalInQueue(int animal_id) {
        // done
        while (true) {
            Animal pred = head;
            Animal curr = pred.next;
            while (curr.animal_id < animal_id) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock.lock();
            try {
                curr.lock.lock();
                try {
                    if (!validate(pred, curr)) {
                        continue;
                    }
                    return curr.animal_id == animal_id;
                } finally {
                    curr.lock.unlock();
                }
            } finally {
                pred.lock.unlock();
            }

        }
    }

    public boolean addAnimal(int animal_id) {
        // done
        while (true) {
            Animal pred = head;
            Animal curr = pred.next;

            while (curr.animal_id < animal_id) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock.lock();
            try {
                curr.lock.lock();
                try {
                    if (!validate(pred, curr)) {
                        continue;
                    }

                    if (curr.animal_id == animal_id) {
                        return false;
                    }

                    Animal newNode = new Animal(animal_id);
                    newNode.next = curr;
                    pred.next = newNode;
                    return true;
                } finally {
                    curr.lock.unlock();
                }
            } finally {
                pred.lock.unlock();
            }
        }
    }

    public boolean feedAnimal(int animal_id) {
        // done
        while (true) {
            Animal pred = head;
            Animal curr = pred.next;

            while (curr.animal_id < animal_id) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock.lock();
            try {
                curr.lock.lock();
                try {
                    if (!validate(pred, curr)) {
                        continue;
                    }

                    if (curr.animal_id != animal_id) {
                        return false;
                    }

                    pred.next = curr.next;
                    return true;
                } finally {
                    curr.lock.unlock();
                }

            } finally {
                pred.lock.unlock();
            }
        }
    }

    public List<Integer> getQueueSnapshot() {
        // done
        // txt page 205-207
        List<Integer> snapshot = new ArrayList<>();

        Animal curr = head.next;
        while (curr != tail) {
            snapshot.add(curr.animal_id);
            curr = curr.next;
        }

        return snapshot;
    }
}