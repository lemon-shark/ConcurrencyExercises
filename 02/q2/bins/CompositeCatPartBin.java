package bins;

import parts.composite.CompositeCatPart;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;

public class CompositeCatPartBin<V extends CompositeCatPart> {
    private long totalLockWaitTime;
    private boolean semaphoreNotSynchronized;
    private Semaphore semaphore;
    private Deque<V> contents;

    public CompositeCatPartBin(boolean semaphoreNotSynchronized) {
        this.semaphoreNotSynchronized = semaphoreNotSynchronized;

        this.totalLockWaitTime = 0;
        this.contents = new ArrayDeque<V>();
        if (semaphoreNotSynchronized) semaphore = new Semaphore(1);
    }

    public V takeOne() {
        if (semaphoreNotSynchronized)
            return takeOneSem();
        else
            return takeOneSynch();
    }
    public void putOne(V v) {
        if (semaphoreNotSynchronized)
            putOneSem(v);
        else
            putOneSynch(v);
    }

    // takeOne and putOne using synchronized
    private V takeOneSynch() {
        long startTime = System.currentTimeMillis();
        synchronized(this) {
            totalLockWaitTime += System.currentTimeMillis();

            startTime = System.currentTimeMillis();
            while (contents.isEmpty()) {
                try { wait(); }
                catch(Exception e){ e.printStackTrace(); }
            }
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return contents.getFirst();
        }
    }
    private void putOneSynch(V v) {
        long startTime = System.currentTimeMillis();
        synchronized(this) {
            totalLockWaitTime += System.currentTimeMillis();

            contents.addLast(v);
            notify();
        }
    }

    // takeOne and putOne using semaphore
    private V takeOneSem() {
        long startTime = System.currentTimeMillis();
        try { semaphore.acquire(); }
        catch(Exception e){ e.printStackTrace(); }
        try {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            startTime = System.currentTimeMillis();
            while (contents.isEmpty()) {
                semaphore.release();
                try { semaphore.acquire(); }
                catch(Exception e){ e.printStackTrace(); }
            }
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return contents.getFirst();
        }
        finally {
            semaphore.release();
        }
    }
    private void putOneSem(V v) {
        long startTime = System.currentTimeMillis();
        try { semaphore.acquire(); }
        catch(Exception e){ e.printStackTrace(); }
        try {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            contents.addLast(v);
        }
        finally {
            semaphore.release();
        }
    }
}
