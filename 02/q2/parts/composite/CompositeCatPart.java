package parts.composite;

import java.util.List;
import java.util.ArrayList;

public class CompositeCatPart extends CatPart {
    private List<CatPart> catParts;

    public CompositeCatPart() {
        this.catParts = new ArrayList<CatPart>();
    }

    public void addCatParts(List<CatPart> catParts) {
        this.catParts.addAll(catParts);
    }
}
