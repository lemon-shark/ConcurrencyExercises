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

public class Main {
    private static final boolean monitor = false;
    private static final boolean semaphore = true;
    private static boolean synchStyle; // will be set to either semaphore or monitor

    String helpString = "" +
        "usage: java Main [--semaphore]" +
        "  --semaphore: optional flag. if flag is provided then use semaphores instead of monitors";

    public static void main(String[] args) {
        /** Parse Command-Line Arguments */
        if (args.length > 1)
            throw new Exception(helpString);
        if (args.length == 1)
            synchStyle = semaphore;
        else
            synchStyle = monitor;

        /** Create All The Bins */

        /** Create All The Robots */
    }
}
