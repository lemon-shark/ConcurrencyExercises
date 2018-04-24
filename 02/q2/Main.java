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

import java.lang.reflect.Constructor;

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
        CompositeCatPartBin<CatComplete>          catCompleteBin =
            new CompositeCatPartBin<CatComplete>(synchStyle);
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

        /** Create All The Robots */
    }
}
