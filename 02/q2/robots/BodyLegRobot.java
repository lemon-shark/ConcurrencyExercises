package robots;

import java.util.ArrayList;

import parts.CatPart;
import parts.base.Body;
import parts.composite.BodyWithTail;
import parts.composite.HindLeg;
import parts.composite.ForeLeg;
import parts.composite.BodyWithLegsTail;
import parts.composite.BodyWithLegs;

import bins.BaseCatPartBin;
import bins.CompositeCatPartBin;
import bins.BinValue;

public class BodyLegRobot extends Robot {
    private final long workTimeMin = 30;
    private final long workTimeMax = 50;

    private BaseCatPartBin<Body> bodyBin;
    private CompositeCatPartBin<BodyWithTail> bodyWithTailBin;
    private CompositeCatPartBin<HindLeg> hindLegBin;
    private CompositeCatPartBin<ForeLeg> foreLegBin;
    private CompositeCatPartBin<BodyWithLegs> bodyWithLegsBin;
    private CompositeCatPartBin<BodyWithLegsTail> bodyWithLegsTailBin;

    public BodyLegRobot(
            BaseCatPartBin<Body> bodyBin,
            CompositeCatPartBin<BodyWithTail> bodyWithTailBin,
            CompositeCatPartBin<HindLeg> hindLegBin,
            CompositeCatPartBin<ForeLeg> foreLegBin
            CompositeCatPartBin<BodyWithLegs> bodyWithLegsBin,
            CompositeCatPartBin<BodyWithLegsTail> bodyWithLegsTailBin
    ) {
        super(workTimeMin, workTimeMax);

        this.bodyBin = bodyBin;
        this.bodyWithTailBin = bodyWithTailBin;
        this.hindLegBin = hindLegBin;
        this.foreLegBin = foreLegBin;
        this.bodyWithLegsBin = bodyWithLegsBin;
        this.bodyWithLegsTailBin = bodyWithLegsTailBin;
    }

    protected void assembleCatPart() {
        long lockWaitTime = 0;

        if (getRandomBoolean()) { // use bodyWithTail
            BinValue<BodyWithTail> bodyWithTailAndTime = bodyWithTailBin.takeOne();
            BinValue<HindLeg> hindLegAndTime1 = hindLegBin.takeOne();
            BinValue<HindLeg> hindLegAndTime2 = hindLegBin.takeOne();
            BinValue<ForeLeg> foreLegAndTime1 = foreLegBin.takeOne();
            BinValue<ForeLeg> foreLegAndTime2 = foreLegBin.takeOne();

            lockWaitTime += bodyWithTailAndTime.getLockWaitTime();
            lockWaitTime += hindLegAndTime1.getLockWaitTime();
            lockWaitTime += hindLegAndTime2.getLockWaitTime();
            lockWaitTime += foreLegAndTime1.getLockWaitTime();
            lockWaitTime += foreLegAndTime2.getLockWaitTime();

            BodyWithLegsTail bodyWithLegsTail = new BodyWithLegsTail();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(bodyWithTailAndTime.getValue());
            catParts.add(hindLegAndTime1.getValue());
            catParts.add(hindLegAndTime2.getValue());
            catParts.add(foreLegAndTime1.getValue());
            catParts.add(foreLegAndTime2.getValue());
            bodyWithLegsTail.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += bodyWithLegsTailBin.putOne(bodyWithLegsTail);
        }
        else { // use body (without tail)
            BinValue<Body> bodyAndTime = bodyBin.takeOne();
            BinValue<HindLeg> hindLegAndTime1 = hindLegBin.takeOne();
            BinValue<HindLeg> hindLegAndTime2 = hindLegBin.takeOne();
            BinValue<ForeLeg> foreLegAndTime1 = foreLegBin.takeOne();
            BinValue<ForeLeg> foreLegAndTime2 = foreLegBin.takeOne();

            lockWaitTime += bodyAndTime.getLockWaitTime();
            lockWaitTime += hindLegAndTime1.getLockWaitTime();
            lockWaitTime += hindLegAndTime2.getLockWaitTime();
            lockWaitTime += foreLegAndTime1.getLockWaitTime();
            lockWaitTime += foreLegAndTime2.getLockWaitTime();

            BodyWithLegs bodyWithLegs = new BodyWithLegs();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(bodyAndTime.getValue());
            catParts.add(hindLegAndTime1.getValue());
            catParts.add(hindLegAndTime2.getValue());
            catParts.add(foreLegAndTime1.getValue());
            catParts.add(foreLegAndTime2.getValue());
            bodyWithLegs.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += bodyWithLegsTailBin.putOne(bodyWithLegsTail);
        }

        addToTotalLockWaitTime(lockWaitTime);
    }
}
