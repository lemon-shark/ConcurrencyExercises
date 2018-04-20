package bins;

public class CompositeCatPartBin<V extends CompositeCatPart> {
    private boolean semaphoreNotSynchronized;

    public CatPartBin(boolean semaphoreNotSynchronized)
    { this.semaphoreNotSynchronized = semaphoreNotSynchronized; }

    public V takeOne() {
        if (semaphoreNotSynchronized)
            return takeOneSemaphore();
        else
            return takeOneSynch();
    }
    public void putOne(V v) {
        if (semaphoreNotSynchronized)
            putOneSem(v);
        else
            putOneSynch(v);
    }

    private V takeOneSynch();
    private void putOneSynch(V v);

    private V takeOneSem();
    private void putOneSem(V v);
}
