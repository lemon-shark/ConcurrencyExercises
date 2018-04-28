import java.util.concurrent.ThreadLocalRandom;

public abstract class Robot extends Thread {
    private boolean shouldKeepRunning = true;
    private long totalLockWaitTime = 0;

    private long workTimeMin;
    private long workTimeMax;

    public Robot(long workTimeMin, long workTimeMax) {
        this.workTimeMin = workTimeMin;
        this.workTimeMax = workTimeMax;
    }

    @Override
    public void run() {
        while (shouldKeepRunning) {
            assembleCatPart();
        }
    }

    protected abstract void assembleCatPart();

    protected void spendTimeWorking()
    { Thread.sleep(ThreadLocalRandom.current().nextLong(workTimeMin, workTimeMax+1)); }

    protected void addToTotalLockWaitTime(long time)
    { this.totalLockWaitTime += time; }

    protected boolean getRandomBoolean()
    { return ThreadLocalRandom.current().nextDouble(1.0) > 0.5; }

    public long getTotalLockWaitTime()
    { return this.totalLockWaitTime; }

    public void turnOff()
    { this.shouldKeepRunning = false; }
}
