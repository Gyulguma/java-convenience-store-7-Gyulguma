package store.model;

import java.util.List;

public class Receipt {
    private final List<Item> items;
    private final int totalPrice;
    private final int discountByPromotion;
    private final int discountByMembership;
    private final int payment;

    public Receipt(List<Item> items, int totalPrice, int discountByPromotion, int discountByMembership, int payment) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.discountByPromotion = discountByPromotion;
        this.discountByMembership = discountByMembership;
        this.payment = payment;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountByPromotion() {
        return discountByPromotion;
    }

    public int getDiscountByMembership() {
        return discountByMembership;
    }

    public int getPayment() {
        return payment;
    }
}
