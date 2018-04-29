package robots;

import java.util.ArrayList;

import parts.CatPart;
import parts.base.Head;
import parts.base.Whisker;
import parts.composite.HeadWithEyes;
import parts.composite.HeadWithWhiskers;
import parts.composite.HeadWithEyesWhiskers;

import bins.BaseCatPartBin;
import bins.CompositeCatPartBin;
import bins.BinValue;

public class HeadWhiskerRobot extends Robot {
    protected final long workTimeMin = 20;
    protected final long workTimeMax = 60;

    private BaseCatPartBin<Head> headBin;
    private BaseCatPartBin<Whisker> whiskerBin;
    private CompositeCatPartBin<HeadWithEyes> headWithEyesBin;
    private CompositeCatPartBin<HeadWithWhiskers> headWithWhiskersBin;
    private CompositeCatPartBin<HeadWithEyesWhiskers> headWithEyesWhiskersBin;

    public HeadWhiskerRobot(
            BaseCatPartBin<Head> headBin,
            BaseCatPartBin<Whisker> whiskerBin,
            CompositeCatPartBin<HeadWithEyes> headWithEyesBin,
            CompositeCatPartBin<HeadWithWhiskers> headWithWhiskersBin,
            CompositeCatPartBin<HeadWithEyesWhiskers> headWithEyesWhiskersBin
    ) {
        this.headBin = headBin;
        this.whiskerBin = whiskerBin;
        this.headWithEyesBin = headWithEyesBin;
        this.headWithWhiskersBin = headWithWhiskersBin;
        this.headWithEyesWhiskersBin = headWithEyesWhiskersBin;
    }

    @Override
    protected void assembleCatPart() {
        long lockWaitTime = 0;

        if (getRandomBoolean()) { // use headWithEyes
            BinValue<HeadWithEyes> headWithEyesAndTime = headWithEyesBin.takeOne();
            BinValue<Whisker> whiskerAndTime1 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime2 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime3 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime4 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime5 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime6 = whiskerBin.takeOne();

            lockWaitTime += headWithEyesAndTime.getLockWaitTime();
            lockWaitTime += whiskerAndTime1.getLockWaitTime();
            lockWaitTime += whiskerAndTime2.getLockWaitTime();
            lockWaitTime += whiskerAndTime3.getLockWaitTime();
            lockWaitTime += whiskerAndTime4.getLockWaitTime();
            lockWaitTime += whiskerAndTime5.getLockWaitTime();
            lockWaitTime += whiskerAndTime6.getLockWaitTime();

            HeadWithEyesWhiskers headWithEyesWhiskers = new HeadWithEyesWhiskers();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(headWithEyesAndTime.getValue());
            catParts.add(whiskerAndTime1.getValue());
            catParts.add(whiskerAndTime2.getValue());
            catParts.add(whiskerAndTime3.getValue());
            catParts.add(whiskerAndTime4.getValue());
            catParts.add(whiskerAndTime5.getValue());
            catParts.add(whiskerAndTime6.getValue());
            headWithEyesWhiskers.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += headWithEyesWhiskersBin.putOne(headWithEyesWhiskers);
        }
        else { // use head (without eyes)
            BinValue<Head> headAndTime = headBin.takeOne();
            BinValue<Whisker> whiskerAndTime1 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime2 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime3 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime4 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime5 = whiskerBin.takeOne();
            BinValue<Whisker> whiskerAndTime6 = whiskerBin.takeOne();

            lockWaitTime += headAndTime.getLockWaitTime();
            lockWaitTime += whiskerAndTime1.getLockWaitTime();
            lockWaitTime += whiskerAndTime2.getLockWaitTime();
            lockWaitTime += whiskerAndTime3.getLockWaitTime();
            lockWaitTime += whiskerAndTime4.getLockWaitTime();
            lockWaitTime += whiskerAndTime5.getLockWaitTime();
            lockWaitTime += whiskerAndTime6.getLockWaitTime();

            HeadWithWhiskers headWithWhiskers = new HeadWithWhiskers();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(headAndTime.getValue());
            catParts.add(whiskerAndTime1.getValue());
            catParts.add(whiskerAndTime2.getValue());
            catParts.add(whiskerAndTime3.getValue());
            catParts.add(whiskerAndTime4.getValue());
            catParts.add(whiskerAndTime5.getValue());
            catParts.add(whiskerAndTime6.getValue());
            headWithWhiskers.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += headWithWhiskersBin.putOne(headWithWhiskers);
        }

        addToTotalLockWaitTime(lockWaitTime);
    }
}
