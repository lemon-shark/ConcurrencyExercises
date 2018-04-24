package bins;

import parts.base.BaseCatPart;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;

import java.lang.reflect.Constructor;

public class BaseCatPartBin<V extends BaseCatPart> {
    private boolean semaphoreNotSynchronized;
    private Semaphore semaphore;
    private Constructor vConstructor;

    public BaseCatPartBin(boolean semaphoreNotSynchronized, Class clazz) {
        this.semaphoreNotSynchronized = semaphoreNotSynchronized;

        try {
            this.vConstructor = clazz.getConstructor();
        } catch(Exception e){
            e.printStackTrace();
        }

        if (semaphoreNotSynchronized) semaphore = new Semaphore(1);
    }

    public BinValue<V> takeOne() {
        if (semaphoreNotSynchronized)
            return takeOneSem();
        else
            return takeOneSynch();
    }

    private BinValue<V> takeOneSynch() {
        long totalLockWaitTime = 0;
        long startTime = System.currentTimeMillis();
        synchronized (this) {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            try {
                // return an object containing the lock wait time and the value itself
                return new BinValue<V>(totalLockWaitTime, (V) vConstructor.newInstance());
            } catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    private BinValue<V> takeOneSem() {
        long totalLockWaitTime = 0;
        long startTime = System.currentTimeMillis();
        try { semaphore.acquire(); }
        catch(Exception e){ e.printStackTrace(); }
        try {
            totalLockWaitTime += System.currentTimeMillis() - startTime;

            try {
                // return an object containing the lock wait time and the value itself
                return new BinValue<V>(totalLockWaitTime, (V) vConstructor.newInstance());
            } catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
        finally {
            semaphore.release();
        }
    }
}
