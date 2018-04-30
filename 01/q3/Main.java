public class Main {
    private static final String helpString = "" +
        "usage: java Main x" +
        "  int x: loop from 0 to Integer.MAX_VALUE/x";

    static long startTime, endTime;

    private static int num;
    private static volatile int numv;

    public static void main(String[] args) throws Exception {
        /** Parse CLI arguments */
        int x = 0;
        if (args.length != 1)
            throw new Exception(helpString);
        try {
            x = Integer.parseInt(args[0]);
        }
        catch (Exception e) {
            throw new Exception(helpString);
        }
        int upperLimit = Integer.MAX_VALUE / x;

        /** Static integer */
        startTime = System.currentTimeMillis();
        for (num = 0; num != upperLimit; num++);
        endTime = System.currentTimeMillis();

        long time1 = endTime - startTime;

        /** Volatile static integer */
        startTime = System.currentTimeMillis();
        for (numv = 0; numv != upperLimit; numv++);
        endTime = System.currentTimeMillis();

        long time2 = endTime - startTime;

        /** synchronized static integer */
        num = 0;
        startTime = System.currentTimeMillis();
        while (num != upperLimit) incrementNum();
        endTime = System.currentTimeMillis();

        long time3 = endTime - startTime;

        /** synchronized static volatile integer, two threads */
        numv = 0;
        Thread t1 = new Thread(() -> { while(numv < upperLimit) incrementNumv();} );
        Thread t2 = new Thread(() -> { while(numv < upperLimit) incrementNumv();} );
        startTime = System.currentTimeMillis();
        t1.start(); t2.start();
        t1.join(); t2.join();
        endTime = System.currentTimeMillis();

        long time4 = endTime - startTime;

        /** static integer, two threads */
        num = 0;
        t1 = new Thread(() -> { while(num < upperLimit) num++; });
        t2 = new Thread(() -> { while(num < upperLimit) num++; });
        startTime = System.currentTimeMillis();
        t1.start(); t2.start();
        t1.join(); t2.join();
        endTime = System.currentTimeMillis();

        long time5 = endTime - startTime;

        /** Print the results */
        System.out.println(time1 + "," + time2 + "," + time3 + "," + time4 + "," + time5);
    }

    public static synchronized void incrementNum() { num++; }
    public static synchronized void incrementNumv() { numv++; }
}
