package bins;

public class BaseCatPartBin<V extends CatPart> {
    private boolean semaphoreNotSynchronized;

    public CatPartBin(boolean semaphoreNotSynchronized)
    { this.semaphoreNotSynchronized = semaphoreNotSynchronized; }

    public V takeOne() {
        if (semaphoreNotSynchronized)
            return takeOneSemaphore();
        else
            return takeOneSynch();
    }

    private V takeOneSynch();
    private V takeOneSem();
}
