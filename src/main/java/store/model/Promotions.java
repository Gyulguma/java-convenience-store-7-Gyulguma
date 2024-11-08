package store.model;

import java.util.ArrayList;
import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = new ArrayList<>(promotions);
    }

    public Promotion findPromotionByName(String name) {
        for (Promotion promotion : promotions) {
            if (promotion.matchName(name)) {
                return promotion;
            }
        }
        return null;
    }
}
