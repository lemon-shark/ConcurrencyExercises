package robots;

import java.util.ArrayList;

import parts.CatPart;
import parts.base.Body;
import parts.base.Tail;
import parts.composite.BodyWithLegs;
import parts.composite.BodyWithTail;
import parts.composite.BodyWithLegsTail;

import bins.BaseCatPartBin;
import bins.CompositeCatPartBin;
import bins.BinValue;

public class BodyTailRobot extends Robot {
    protected final long workTimeMin = 10;
    protected final long workTimeMax = 20;

    private BaseCatPartBin<Body> bodyBin;
    private BaseCatPartBin<Tail> tailBin;
    private CompositeCatPartBin<BodyWithLegs> bodyWithLegsBin;
    private CompositeCatPartBin<BodyWithTail> bodyWithTailBin;
    private CompositeCatPartBin<BodyWithLegsTail> bodyWithLegsTailBin;

    public BodyTailRobot(
            String name,
            BaseCatPartBin<Body> bodyBin,
            BaseCatPartBin<Tail> tailBin,
            CompositeCatPartBin<BodyWithLegs> bodyWithLegsBin,
            CompositeCatPartBin<BodyWithTail> bodyWithTailBin,
            CompositeCatPartBin<BodyWithLegsTail> bodyWithLegsTailBin
    ) {
        super(name);

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
            catParts.add(tailAndTime.getValue());
            bodyWithLegsTail.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += bodyWithLegsTailBin.putOne(bodyWithLegsTail);
        }
        else { // use body (without legs)
            BinValue<Body> bodyAndTime = bodyBin.takeOne();
            BinValue<Tail> tailAndTime = tailBin.takeOne();

            lockWaitTime += bodyAndTime.getLockWaitTime();
            lockWaitTime += tailAndTime.getLockWaitTime();

            BodyWithLegs bodyWithLegs = new BodyWithLegs();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(bodyAndTime.getValue());
            catParts.add(tailAndTime.getValue());
            bodyWithLegs.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += bodyWithLegsBin.putOne(bodyWithLegs);
        }

        addToTotalLockWaitTime(lockWaitTime);
    }
}
