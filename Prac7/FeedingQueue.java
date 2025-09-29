import java.util.concurrent.locks.ReentrantLock;
import java.util.*;  // for List, ArrayList


public class FeedingQueue {
    private final Animal head;
    private final Animal tail;

    public FeedingQueue() {
        head = new Animal(Integer.MIN_VALUE);
        tail = new Animal(Integer.MAX_VALUE);
        head.next = tail;
    }

    private boolean validate(Animal pred, Animal curr) {
        //TODO: complete function
    }

    public boolean isAnimalInQueue(int animal_id) {
        //TODO: complete function
    }

    public boolean addAnimal(int animal_id) {
        //TODO: complete function
    }

    public boolean feedAnimal(int animal_id) {
        //TODO: complete function
    }

    public List<Integer> getQueueSnapshot() {
        //TODO: complete function
    }
}