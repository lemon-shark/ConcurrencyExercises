package robots;

import java.util.ArrayList;

import parts.CatPart;
import parts.composite.HeadWithEyesWhiskers;
import parts.composite.BodyWithLegsTail;
import parts.composite.CatComplete;

import bins.CompositeCatPartBin;
import bins.BinValue;

public class CompleteCatRobot extends Robot {
    private final int NUM_CATS_TO_BUILD = 250;

    protected final long workTimeMin = 10;
    protected final long workTimeMax = 20;

    private int count = 0;

    private CompositeCatPartBin<HeadWithEyesWhiskers> headBin;
    private CompositeCatPartBin<BodyWithLegsTail> bodyBin;
    private CompositeCatPartBin<CatComplete> fullCatBin;

    public CompleteCatRobot(
            CompositeCatPartBin<HeadWithEyesWhiskers> headBin,
            CompositeCatPartBin<BodyWithLegsTail> bodyBin,
            CompositeCatPartBin<CatComplete> fullCatBin
    ) {
        this.headBin = headBin;
        this.bodyBin = bodyBin;
        this.fullCatBin = fullCatBin;
    }

    protected void assembleCatPart() {
        if (++this.count == NUM_CATS_TO_BUILD) turnOff();

        BinValue<HeadWithEyesWhiskers> headAndTime = headBin.takeOne();
        BinValue<BodyWithLegsTail> bodyAndTime = bodyBin.takeOne();

        long lockWaitTime = 0;
        lockWaitTime += headAndTime.getLockWaitTime();
        lockWaitTime += bodyAndTime.getLockWaitTime();

        CatComplete fullCat = new CatComplete();
        ArrayList<CatPart> catParts = new ArrayList<>();
        catParts.add(headAndTime.getValue());
        catParts.add(bodyAndTime.getValue());
        fullCat.addCatParts(catParts);

        spendTimeWorking();

        lockWaitTime += fullCatBin.putOne(fullCat);
        addToTotalLockWaitTime(lockWaitTime);
    }
}
