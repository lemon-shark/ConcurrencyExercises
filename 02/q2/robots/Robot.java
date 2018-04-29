package robots;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Robot extends Thread {
    protected boolean shouldKeepRunning = true;
    protected long totalLockWaitTime = 0;

    // override this in subclasses
    protected final long workTimeMin = 0;
    protected final long workTimeMax = 0;

    protected final String name;
    public Robot(String name) { this.name = name; }

    @Override
    public void run() {
        while (shouldKeepRunning) {
            assembleCatPart();
        }
    }

    protected abstract void assembleCatPart();

    protected void spendTimeWorking() {
        long randomLongInRange =
            ThreadLocalRandom.current().nextLong(workTimeMin, workTimeMax+1);
        try { Thread.sleep(randomLongInRange);
        } catch(Exception e){ e.printStackTrace(); }
    }

    protected void addToTotalLockWaitTime(long time)
    { this.totalLockWaitTime += time; }

    protected boolean getRandomBoolean()
    { return ThreadLocalRandom.current().nextDouble(1.0) > 0.5; }

    public long getTotalLockWaitTime()
    { return this.totalLockWaitTime; }

    public void turnOff()
    { this.shouldKeepRunning = false; }

    public String getRobotName()
    { return this.name; }
}
