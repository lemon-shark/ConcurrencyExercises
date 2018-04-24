public abstract class Robot extends Thread {
    private boolean shouldKeepRunning = true;
    private long totalLockWaitTime = 0;

    @Override
    public void run() {
        while (shouldKeepRunning) {
            assembleCatPart();
        }
    }

    abstract void assembleCatPart();

    public void turnOff()
    { this.shouldKeepRunning = false; }

    public long getTotalLockWaitTime()
    { return this.totalLockWaitTime; }
}
