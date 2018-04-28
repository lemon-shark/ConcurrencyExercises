package parts.composite;

import parts.CatPart;

import java.util.List;
import java.util.ArrayList;

public class CompositeCatPart extends CatPart {
    private List<CatPart> catParts = new ArrayList<CatPart>();

    public void addCatParts(List<CatPart> catParts)
    { this.catParts.addAll(catParts); }
}
