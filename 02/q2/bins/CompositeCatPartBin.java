package bins;

import parts.composite.CompositeCatPart;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;

public class CompositeCatPartBin<V extends CompositeCatPart> {
    private boolean semaphoreNotSynchronized;
    private Semaphore semaphore;
    private Deque<V> contents;

    public CompositeCatPartBin(boolean semaphoreNotSynchronized) {
        this.semaphoreNotSynchronized = semaphoreNotSynchronized;

        this.contents = new ArrayDeque<V>();
        if (semaphoreNotSynchronized) semaphore = new Semaphore(1);
    }

    public BinValue<V> takeOne() {
        if (semaphoreNotSynchronized)
            return takeOneSem();
        else
            return takeOneSynch();
    }
    public long putOne(V v) {
        if (semaphoreNotSynchronized)
            return putOneSem(v);
        else
            return putOneSynch(v);
    }

    // takeOne and putOne using synchronized
    private BinValue<V> takeOneSynch() {
        long startTime = System.currentTimeMillis();
        long totalLockWaitTime = 0;
        synchronized(this) {
            while (contents.isEmpty()) {
                try { wait(); }
                catch(Exception e){ e.printStackTrace(); }
            }
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return new BinValue<V>(totalLockWaitTime, contents.getFirst());
        }
    }
    private long putOneSynch(V v) {
        long startTime = System.currentTimeMillis();
        long totalLockWaitTime = 0;
        synchronized(this) {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            contents.addLast(v);
            notify();

            return totalLockWaitTime;
        }
    }

    // takeOne and putOne using semaphore
    private BinValue<V> takeOneSem() {
        long startTime = System.currentTimeMillis();
        long totalLockWaitTime = 0;
        try { semaphore.acquire(); }
        catch(Exception e){ e.printStackTrace(); }
        try {
            while (contents.isEmpty()) {
                semaphore.release();
                try { semaphore.acquire(); }
                catch(Exception e){ e.printStackTrace(); }
            }
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            return new BinValue<V>(totalLockWaitTime, contents.getFirst());
        }
        finally {
            semaphore.release();
        }
    }
    private long putOneSem(V v) {
        long startTime = System.currentTimeMillis();
        long totalLockWaitTime = 0;
        try { semaphore.acquire(); }
        catch(Exception e){ e.printStackTrace(); }
        try {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            contents.addLast(v);

            return totalLockWaitTime;
        }
        finally {
            semaphore.release();
        }
    }
}
