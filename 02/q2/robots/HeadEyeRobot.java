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
    private final long workTimeMin = 10;
    private final long workTimeMax = 30;

    private BaseCatPartBin<Head> headBin;
    private BaseCatPartBin<Eye> eyeBin;
    private CompositeCatPartBin<HeadWithEyes> headWithEyesBin;
    private CompositeCatPartBin<HeadWithWhiskers> headWithWhiskerBin;
    private CompositeCatPartBin<HeadWithEyesWhiskers> headWithEyesWhiskersBin;

    public HeadEyeRobot(
            BaseCatPartBin<Head> headBin,
            BaseCatPartBin<Eye> eyeBin,
            CompositeCatPartBin<HeadWithEyes> headWithEyesBin,
            CompositeCatPartBin<HeadWithWhiskers> headWithWhiskerBin,
            CompositeCatPartBin<HeadWithEyesWhiskers> headWithEyesWhiskersBin
    ) {
        super(workTimeMin, workTimeMax);

        this.headBin = headBin;
        this.eyeBin = eyeBin;
        this.headWithEyesBin = headWithEyesBin;
        this.headWithWhiskersBin = headWithWhiskersBin;
        this.headWithEyesWhiskersBin = headWithEyesWhiskersBin;
    }

    protected void assembleCatPart() {
        // TODO
    }
}
