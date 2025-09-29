import java.util.concurrent.locks.ReentrantLock;

public class Animal {
    int animal_id;
    Animal next;
    final ReentrantLock lock = new ReentrantLock();

    public Animal(int animal_id) {
        this.animal_id = animal_id;
    }
}
