package bins;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;

public class BaseCatPartBin<V extends CatPart> {
    private volatile long totalLockWaitTime;
    private boolean semaphoreNotSynchronized;
    private Semaphore semaphore;

    public CatPartBin(boolean semaphoreNotSynchronized) {
        this.semaphoreNotSynchronized = semaphoreNotSynchronized;

        this.totalLockWaitTime = 0;
        this.contents = new ArrayList<V>();
        if (semaphoreNotSynchronized) semaphore = new Semaphore(1);
    }

    public V takeOne() {
        if (semaphoreNotSynchronized)
            return takeOneSemaphore();
        else
            return takeOneSynch();
    }

    private V takeOneSynch() {
        long startTime = System.currentTimeMillis();
        synchronized (this) {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return V.createInstance();
        }
    }

    private V takeOneSem() {
        long startTime = System.currentTimeMillis();
        semaphore.acquire();
        try {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return new V.createInstance();
        }
        finally {
            semaphore.release();
        }
    }
}
