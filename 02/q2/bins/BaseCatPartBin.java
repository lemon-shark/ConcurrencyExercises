package bins;

import parts.base.BaseCatPart;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;

public class BaseCatPartBin<V extends BaseCatPart> {
    private volatile long totalLockWaitTime;
    private boolean semaphoreNotSynchronized;
    private Semaphore semaphore;

    public BaseCatPartBin(boolean semaphoreNotSynchronized) {
        this.semaphoreNotSynchronized = semaphoreNotSynchronized;

        this.totalLockWaitTime = 0;
        if (semaphoreNotSynchronized) semaphore = new Semaphore(1);
    }

    public V takeOne() {
        if (semaphoreNotSynchronized)
            return takeOneSem();
        else
            return takeOneSynch();
    }

    private V takeOneSynch() {
        long startTime = System.currentTimeMillis();
        synchronized (this) {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return (V) V.createInstance();
        }
    }

    private V takeOneSem() {
        long startTime = System.currentTimeMillis();
        try { semaphore.acquire(); }
        catch(Exception e){ e.printStackTrace(); }
        try {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return (V) V.createInstance();
        }
        finally {
            semaphore.release();
        }
    }
}
