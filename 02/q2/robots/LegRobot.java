package robots;

import java.util.ArrayList;

import parts.CatPart;
import parts.base.Leg;
import parts.base.Toe;
import parts.composite.HindLeg;
import parts.composite.ForeLeg;

import bins.BaseCatPartBin;
import bins.CompositeCatPartBin;
import bins.BinValue;

public class LegRobot extends Robot {
    protected final long workTimeMin = 30;
    protected final long workTimeMax = 50;

    private BaseCatPartBin<Leg> legBin;
    private BaseCatPartBin<Toe> toeBin;
    private CompositeCatPartBin<HindLeg> hindLegBin;
    private CompositeCatPartBin<ForeLeg> foreLegBin;

    public LegRobot(
            String name,
            BaseCatPartBin<Leg> legBin,
            BaseCatPartBin<Toe> toeBin,
            CompositeCatPartBin<HindLeg> hindLegBin,
            CompositeCatPartBin<ForeLeg> foreLegBin
    ) {
        super(name);

        this.legBin = legBin;
        this.toeBin = toeBin;
        this.hindLegBin = hindLegBin;
        this.foreLegBin = foreLegBin;
    }

    protected void assembleCatPart() {
        long lockWaitTime = 0;

        if (getRandomBoolean()) { // 4 toes - foreLeg
            BinValue<Leg> legAndTime = legBin.takeOne();
            BinValue<Toe> toeAndTime1 = toeBin.takeOne();
            BinValue<Toe> toeAndTime2 = toeBin.takeOne();
            BinValue<Toe> toeAndTime3 = toeBin.takeOne();
            BinValue<Toe> toeAndTime4 = toeBin.takeOne();

            lockWaitTime += legAndTime.getLockWaitTime();
            lockWaitTime += toeAndTime1.getLockWaitTime();
            lockWaitTime += toeAndTime2.getLockWaitTime();
            lockWaitTime += toeAndTime3.getLockWaitTime();
            lockWaitTime += toeAndTime4.getLockWaitTime();

            ForeLeg foreLeg = new ForeLeg();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(legAndTime.getValue());
            catParts.add(toeAndTime1.getValue());
            catParts.add(toeAndTime2.getValue());
            catParts.add(toeAndTime3.getValue());
            catParts.add(toeAndTime4.getValue());
            foreLeg.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += foreLegBin.putOne(foreLeg);
        }
        else { // 5 toes - hindLeg
            BinValue<Leg> legAndTime = legBin.takeOne();
            BinValue<Toe> toeAndTime1 = toeBin.takeOne();
            BinValue<Toe> toeAndTime2 = toeBin.takeOne();
            BinValue<Toe> toeAndTime3 = toeBin.takeOne();
            BinValue<Toe> toeAndTime4 = toeBin.takeOne();
            BinValue<Toe> toeAndTime5 = toeBin.takeOne();

            lockWaitTime += legAndTime.getLockWaitTime();
            lockWaitTime += toeAndTime1.getLockWaitTime();
            lockWaitTime += toeAndTime2.getLockWaitTime();
            lockWaitTime += toeAndTime3.getLockWaitTime();
            lockWaitTime += toeAndTime4.getLockWaitTime();
            lockWaitTime += toeAndTime5.getLockWaitTime();

            HindLeg hindLeg = new HindLeg();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(legAndTime.getValue());
            catParts.add(toeAndTime1.getValue());
            catParts.add(toeAndTime2.getValue());
            catParts.add(toeAndTime3.getValue());
            catParts.add(toeAndTime4.getValue());
            catParts.add(toeAndTime5.getValue());
            hindLeg.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += hindLegBin.putOne(hindLeg);
        }

        addToTotalLockWaitTime(lockWaitTime);
    }

    protected long getWorkTimeMin() { return this.workTimeMin; }
    protected long getWorkTimeMax() { return this.workTimeMax; }
}
