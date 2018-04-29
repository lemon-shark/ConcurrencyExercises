package robots;

import java.util.ArrayList;

import parts.CatPart;
import parts.base.Head;
import parts.base.Eye;
import parts.composite.HeadWithWhiskers;
import parts.composite.HeadWithEyes;
import parts.composite.HeadWithEyesWhiskers;

import bins.BaseCatPartBin;
import bins.CompositeCatPartBin;
import bins.BinValue;

public class HeadEyeRobot extends Robot {
    protected final long workTimeMin = 10;
    protected final long workTimeMax = 30;

    private BaseCatPartBin<Head> headBin;
    private BaseCatPartBin<Eye> eyeBin;
    private CompositeCatPartBin<HeadWithEyes> headWithEyesBin;
    private CompositeCatPartBin<HeadWithWhiskers> headWithWhiskersBin;
    private CompositeCatPartBin<HeadWithEyesWhiskers> headWithEyesWhiskersBin;

    public HeadEyeRobot(
            BaseCatPartBin<Head> headBin,
            BaseCatPartBin<Eye> eyeBin,
            CompositeCatPartBin<HeadWithEyes> headWithEyesBin,
            CompositeCatPartBin<HeadWithWhiskers> headWithWhiskersBin,
            CompositeCatPartBin<HeadWithEyesWhiskers> headWithEyesWhiskersBin
    ) {
        this.headBin = headBin;
        this.eyeBin = eyeBin;
        this.headWithEyesBin = headWithEyesBin;
        this.headWithWhiskersBin = headWithWhiskersBin;
        this.headWithEyesWhiskersBin = headWithEyesWhiskersBin;
    }

    protected void assembleCatPart() {
        long lockWaitTime = 0;

        if (getRandomBoolean()) { // use headWithWhiskers
            BinValue<HeadWithWhiskers> headWithWhiskersAndTime = headWithWhiskersBin.takeOne();
            BinValue<Eye> eyeAndTime1 = eyeBin.takeOne();
            BinValue<Eye> eyeAndTime2 = eyeBin.takeOne();

            lockWaitTime += headWithWhiskersAndTime.getLockWaitTime();
            lockWaitTime += eyeAndTime1.getLockWaitTime();
            lockWaitTime += eyeAndTime2.getLockWaitTime();

            HeadWithEyesWhiskers headWithEyesWhiskers = new HeadWithEyesWhiskers();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(headWithWhiskersAndTime.getValue());
            catParts.add(eyeAndTime1.getValue());
            catParts.add(eyeAndTime2.getValue());
            headWithEyesWhiskers.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += headWithEyesWhiskersBin.putOne(headWithEyesWhiskers);
        }
        else { // ues head (without whiskers)
            BinValue<Head> headAndTime = headBin.takeOne();
            BinValue<Eye> eyeAndTime1 = eyeBin.takeOne();
            BinValue<Eye> eyeAndTime2 = eyeBin.takeOne();

            lockWaitTime += headAndTime.getLockWaitTime();
            lockWaitTime += eyeAndTime1.getLockWaitTime();
            lockWaitTime += eyeAndTime2.getLockWaitTime();

            HeadWithEyes headWithEyes = new HeadWithEyes();
            ArrayList<CatPart> catParts = new ArrayList<>();
            catParts.add(headAndTime.getValue());
            catParts.add(eyeAndTime1.getValue());
            catParts.add(eyeAndTime2.getValue());
            headWithEyes.addCatParts(catParts);

            spendTimeWorking();

            lockWaitTime += headWithEyesBin.putOne(headWithEyes);
        }

        addToTotalLockWaitTime(lockWaitTime);
    }
}
