import parts.base.Body;
import parts.base.Eye;
import parts.base.Head;
import parts.base.Leg;
import parts.base.Tail;
import parts.base.Toe;
import parts.base.Whisker;

import parts.composite.BodyWithLegs;
import parts.composite.BodyWithLegsTail;
import parts.composite.BodyWithTail;
import parts.composite.CatComplete;
import parts.composite.ForeLeg;
import parts.composite.HeadWithEyes;
import parts.composite.HeadWithEyesWhiskers;
import parts.composite.HeadWithWhiskers;
import parts.composite.HindLeg;

import bins.BaseCatPartBin;
import bins.CompositeCatPartBin;
import bins.BinValue;

import robots.BodyLegRobot;
import robots.BodyTailRobot;
import robots.CompleteCatRobot;
import robots.HeadEyeRobot;
import robots.HeadWhiskerRobot;
import robots.LegRobot;
import robots.Robot;

public class Main {
    private static final boolean monitor = false;
    private static final boolean semaphore = true;
    private static boolean synchStyle; // will be set to either semaphore or monitor

    private static String helpString = "" +
        "usage: java Main [--semaphore]" +
        "  --semaphore: optional flag. if flag is provided then use semaphores instead of monitors";

    public static void main(String[] args) throws Exception {
        /** Parse Command-Line Arguments */
        if (args.length > 1)
            throw new Exception(helpString);
        if (args.length == 1)
            synchStyle = semaphore;
        else
            synchStyle = monitor;

        /** Create All The Bins */
        BaseCatPartBin<Body>    bodyBin =
            new BaseCatPartBin<Body>(synchStyle, Body.class);
        BaseCatPartBin<Eye>     eyeBin =
            new BaseCatPartBin<Eye>(synchStyle, Eye.class);
        BaseCatPartBin<Head>    headBin =
            new BaseCatPartBin<Head>(synchStyle, Head.class);
        BaseCatPartBin<Leg>     legBin =
            new BaseCatPartBin<Leg>(synchStyle, Leg.class);
        BaseCatPartBin<Tail>    tailBin =
            new BaseCatPartBin<Tail>(synchStyle, Tail.class);
        BaseCatPartBin<Toe>     toeBin =
            new BaseCatPartBin<Toe>(synchStyle, Toe.class);
        BaseCatPartBin<Whisker> whiskerBin =
            new BaseCatPartBin<Whisker>(synchStyle, Whisker.class);

        CompositeCatPartBin<BodyWithLegs>         bodyWithLegsBin =
            new CompositeCatPartBin<BodyWithLegs>(synchStyle);
        CompositeCatPartBin<BodyWithLegsTail>     bodyWithLegsTailBin =
            new CompositeCatPartBin<BodyWithLegsTail>(synchStyle);
        CompositeCatPartBin<BodyWithTail>         bodyWithTailBin =
            new CompositeCatPartBin<BodyWithTail>(synchStyle);
        CompositeCatPartBin<ForeLeg>              foreLegBin =
            new CompositeCatPartBin<ForeLeg>(synchStyle);
        CompositeCatPartBin<HeadWithEyes>         headWithEyesBin =
            new CompositeCatPartBin<HeadWithEyes>(synchStyle);
        CompositeCatPartBin<HeadWithEyesWhiskers> headWithEyesWhiskersBin =
            new CompositeCatPartBin<HeadWithEyesWhiskers>(synchStyle);
        CompositeCatPartBin<HeadWithWhiskers>     headWithWhiskersBin =
            new CompositeCatPartBin<HeadWithWhiskers>(synchStyle);
        CompositeCatPartBin<HindLeg>              hindLegBin =
            new CompositeCatPartBin<HindLeg>(synchStyle);
        CompositeCatPartBin<CatComplete>          catCompleteBin =
            new CompositeCatPartBin<CatComplete>(synchStyle);

        /** Create All The Robots */
        LegRobot legRobot1 = new LegRobot(
                "legRobot1",
                legBin,
                toeBin,
                hindLegBin,
                foreLegBin
        );
        LegRobot legRobot2 = new LegRobot(
                "legRobot2",
                legBin,
                toeBin,
                hindLegBin,
                foreLegBin
        );
        BodyLegRobot bodyLegRobot1 = new BodyLegRobot(
                "bodyLegRobot1",
                bodyBin,
                bodyWithTailBin,
                hindLegBin,
                foreLegBin,
                bodyWithLegsBin,
                bodyWithLegsTailBin
        );
        BodyLegRobot bodyLegRobot2 = new BodyLegRobot(
                "bodyLegRobot2",
                bodyBin,
                bodyWithTailBin,
                hindLegBin,
                foreLegBin,
                bodyWithLegsBin,
                bodyWithLegsTailBin
        );
        BodyTailRobot bodyTailRobot1 = new BodyTailRobot(
                "bodyTailRobot1",
                bodyBin,
                tailBin,
                bodyWithLegsBin,
                bodyWithTailBin,
                bodyWithLegsTailBin
        );
        BodyTailRobot bodyTailRobot2 = new BodyTailRobot(
                "bodyTailRobot2",
                bodyBin,
                tailBin,
                bodyWithLegsBin,
                bodyWithTailBin,
                bodyWithLegsTailBin
        );
        HeadEyeRobot headEyeRobot1 = new HeadEyeRobot(
                "headEyeRobot1",
                headBin,
                eyeBin,
                headWithEyesBin,
                headWithWhiskersBin,
                headWithEyesWhiskersBin
        );
        HeadEyeRobot headEyeRobot2 = new HeadEyeRobot(
                "headEyeRobot2",
                headBin,
                eyeBin,
                headWithEyesBin,
                headWithWhiskersBin,
                headWithEyesWhiskersBin
        );
        HeadWhiskerRobot headWhiskerRobot1 = new HeadWhiskerRobot(
                "headWhiskerRobot1",
                headBin,
                whiskerBin,
                headWithEyesBin,
                headWithWhiskersBin,
                headWithEyesWhiskersBin
        );
        HeadWhiskerRobot headWhiskerRobot2 = new HeadWhiskerRobot(
                "headWhiskerRobot2",
                headBin,
                whiskerBin,
                headWithEyesBin,
                headWithWhiskersBin,
                headWithEyesWhiskersBin
        );

        CompleteCatRobot completeCatRobot = new CompleteCatRobot(
                "CompleteCatRobot",
                headWithEyesWhiskersBin,
                bodyWithLegsTailBin,
                catCompleteBin
        );

        Robot[] robots = new Robot[] {
            legRobot1,
            legRobot2,
            bodyLegRobot1,
            bodyLegRobot2,
            bodyTailRobot1,
            bodyTailRobot2,
            headEyeRobot1,
            headEyeRobot2,
            headWhiskerRobot1,
            headWhiskerRobot2,
        };

        long startTime = System.currentTimeMillis();

        /** Start all the robots */
        for (Robot robot : robots)
            robot.start();
        completeCatRobot.start();

        /** Wait for the CompleteCatRobot to finish, then stop all robots */
        completeCatRobot.join();

        long endTime = System.currentTimeMillis();

        for (Robot robot : robots)
            robot.turnOff();

        /** Output the results of the experiment */
        System.out.print("totalTime");
        for (Robot robot : robots)
            System.out.print("," + robot.getRobotName());
        System.out.println("," + completeCatRobot.getRobotName());

        System.out.print(endTime - startTime);
        for (Robot robot : robots)
            System.out.print("," + robot.getTotalLockWaitTime());
        System.out.println("," + completeCatRobot.getTotalLockWaitTime());
    }
}
