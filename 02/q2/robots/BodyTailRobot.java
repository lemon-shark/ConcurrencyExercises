package robots;

import java.util.ArrayList;

import parts.CatPart;
import parts.base.Body;
import parts.base.Tail;
import parts.composite.BodyWithLegs;
import parts.composite.BodyWithTail;

import bins.BaseCatPartBin;
import bins.CompositeCatPartBin;
import bins.BinValue;

public class BodyTailRobot extends Robot {
    private final long workTimeMin = 10;
    private final long workTimeMax = 20;

    private BaseCatPart<Body> bodyBin;
    private BaseCatPartBin<Tail> tailBin;
    private CompositeCatPartBin<BodyWithLegs> bodyWithLegsBin;
    private CompositeCatPartBin<BodyWithTail> bodyWithTailBin;
    private CompositeCatPartBin<BodyWithLegsTail> bodyWithLegsTailBin;

    public BodyTailRobot(
            BaseCatPart<Body> bodyBin,
            BaseCatPartBin<Tail> tailBin,
            CompositeCatPartBin<BodyWithLegs> bodyWithLegsBin,
            CompositeCatPartBin<BodyWithTail> bodyWithTailBin,
            CompositeCatPartBin<BodyWithLegsTail> bodyWithLegsTailBin
    ) {
        super(workTimeMin, workTimeMax);

        this.bodyBin = bodyBin;
        this.tailBin = tailBin;
        this.bodyWithLegsBin = bodyWithLegsBin;
        this.bodyWithTailBin = bodyWithTailBin;
        this.bodyWithLegsTailBin = bodyWithLegsTailBin;
    }

    protected void assembleCatPart() {
        long lockWaitTime = 0;

        if (getRandomBoolean()) { // use bodyWithLegs
            BinValue<BodyWithLegs> bodyWithLegsAndTime = bodyWithLegsBin.takeOne();
            BinValue<Tail> tailAndTime = tailBin.takeOne();

            lockWaitTime += bodyWithLegsAndTime.getLockWaitTime();
            lockWaitTime += tailAndTime.getLockWaitTime();

            BodyWithLegsTail bodyWithLegsTail = new BodyWithLegsTail();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(bodyWithLegsAndTime.getValue());
            catParts.add(tailAndtime.getValue());
            bodyWithLegsTail.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += bodyWithLegsTailBin.putOne(bodyWithLegsTail);
        }
        else {
            BinValue<Body> bodyAndTime = bodyBin.takeOne();
            BinVale<Tail> tailAndTime = tailBin.takeOne();

            lockWaitTime += bodyAndTime.getLockWaitTime();
            lockWaitTime += tailAndTime.getLockWaitTime();

            BodyWithLegs bodyWithLegs = new BodyWithLegs();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(bodyAndTime.getValue());
            catParts.add(tailAndTime.getValue());
            bodyWithLegs.addCarParts(catParts);

            spendTimeWorking();

            lockWaitTime += bodyWithLegsBin.putOne(bodyWithLegs);
        }

        addToTotalLockWaitTime(lockWaitTime);
    }
}
