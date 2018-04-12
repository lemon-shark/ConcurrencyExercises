import java.util.concurrent.locks.ReentrantLock;

public class GameGridCell {
    private ReentrantLock lock;

    public volatile GameElement occupant;// a cell can be occupied by a GameElement

    public GameGridCell() {
        lock = new ReentrantLock();
        occupant = null;
    }

    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }
    public boolean tryLock() { return lock.tryLock(); }
}
