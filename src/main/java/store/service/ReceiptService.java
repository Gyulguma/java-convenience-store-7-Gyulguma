package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import store.model.Item;
import store.model.Product;
import store.model.Products;
import store.model.Receipt;
import store.util.constants.ServiceConstants;

public class ReceiptService {
    public ReceiptService() {
    }

    public Receipt createReceipt(Products products, List<Item> items, boolean applyMembership) {
        int totalPrice = getTotalPrice(products, items);
        int discountByPromotion = getPromotionDiscount(products, items);
        int discountByMembership = 0;
        if (applyMembership) {
            discountByMembership = getMembershipDiscount(products, items);
        }
        int payment = totalPrice - discountByPromotion - discountByMembership;
        return new Receipt(items, totalPrice, discountByPromotion, discountByMembership, payment);
    }

    private int getTotalPrice(Products products, List<Item> items) {
        int totalPrice = 0;
        for (Item item : items) {
            totalPrice += getPrice(products, item);
        }
        return totalPrice;
    }

    private int getPrice(Products products, Item item) {
        Product product = products.findProductByName(item.getName());
        return item.getQuantity() * product.getPrice();
    }


    public int getPromotionDiscount(Products products, List<Item> items) {
        int discount = 0;
        for (Item item : items) {
            discount += getPriceForFreeByPromotion(products, item);
        }
        return discount;
    }

    private int getPriceForFreeByPromotion(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (product == null || !product.isApplyPromotion(DateTimes.now())) {
            return 0;
        }
        int price = product.getPrice();
        int getFreeCount = product.getMaxCanGetForFreeByPromotion(item.getQuantity());
        return getFreeCount * price;
    }

    public int getMembershipDiscount(Products products, List<Item> items) {
        int totalPriceNotApplyPromotion = 0;
        for (Item item : items) {
            totalPriceNotApplyPromotion += getPriceNotApplyPromotion(products, item);
        }
        int discount = (int) (totalPriceNotApplyPromotion * ServiceConstants.MEMBERSHIP_DISCOUNT_RATE);
        if (discount > ServiceConstants.MEMBERSHIP_DISCOUNT_LIMIT) {
            discount = ServiceConstants.MEMBERSHIP_DISCOUNT_LIMIT;
        }
        return discount;
    }

    private int getPriceNotApplyPromotion(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (product == null || !product.isApplyPromotion(DateTimes.now())) {
            product = products.findProductByNameAndPromotionIsNull(item.getName());
            return item.getQuantity() * product.getPrice();
        }
        int price = product.getPrice();
        int applyPromotion = product.getMaxCanGetForFreeByPromotion(item.getQuantity()) * (product.getPromotionGet()+product.getPromotion()
                .getBuy());
        int notApplyPromotionCount = item.getQuantity() - applyPromotion;
        return notApplyPromotionCount * price;
    }
}
