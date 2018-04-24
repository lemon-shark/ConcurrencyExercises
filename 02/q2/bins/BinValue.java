package bins;

import parts.CatPart;

/**
 * A container to hold values acquired from
 * CatPartBins AND the time spent waiting
 * on locks to get them.
 */
public class BinValue<V extends CatPart> {
    private final long lockWaitTime;
    private final V binValue;

    public BinValue(long lockWaitTime, V binValue) {
        this.lockWaitTime = lockWaitTime;
        this.binValue = binValue;
    }

    public long getLockWaitTime()
    { return this.lockWaitTime; }

    public V getValue()
    { return this.binValue; }
}
