package store.model;

public class Receipt {
    private final int totalPrice;
    private final int discountByPromotion;
    private final int discountByMembership;
    private final int payment;

    public Receipt(int totalPrice, int discountByPromotion, int discountByMembership, int payment) {
        this.totalPrice = totalPrice;
        this.discountByPromotion = discountByPromotion;
        this.discountByMembership = discountByMembership;
        this.payment = payment;
    }
}
