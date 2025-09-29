
class Task {
    enum Type { ADD, FEED }
    Type type;
    int animalId;

    Task(Type type, int animalId) {
        this.type = type;
        this.animalId = animalId;
    }
}

class Zookeeper extends Thread {
    private final int id;
    private final FeedingQueue list;
    private final Task[] tasks;

    public Zookeeper (int id, FeedingQueue list, Task[] tasks) {
        this.id = id;
        this.list = list;
        this.tasks = tasks;
    }

    public void run() {
        for (Task op : tasks) {
            switch (op.type) {
                case ADD : {
                    if (list.addAnimal(op.animalId)) {
                        System.out.println("Zookeeper " + id + " added animal #" + op.animalId + " to the queue");
                    } else {
                        System.out.println("Zookeeper " + id + " tried to add animal #" + op.animalId + " but it's already in the queue");
                    }
                }
                case FEED : {
                    if (list.feedAnimal(op.animalId)) {
                        System.out.println("Zookeeper " + id + " fed animal #" + op.animalId);
                    } else {
                        System.out.println("Zookeeper " + id + " tried to feed animal #" + op.animalId + " but it wasn't in the queue!");
                    }
                }
            }
        }
    }
}