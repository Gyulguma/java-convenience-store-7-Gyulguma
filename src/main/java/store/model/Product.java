package store.model;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

public class Product {
    private static final String PRICE_UNIT = "원";
    private static final String STOCK_UNIT = "%d개";
    private static final String OUT_OF_STOCK = "재고 없음";

    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ");
        sb.append(formatCurrency(price)).append(" ");
        String stock = String.format(STOCK_UNIT, quantity);
        if (quantity == 0) {
            stock = OUT_OF_STOCK;
        }
        sb.append(stock).append(" ");
        if (promotion != null) {
            sb.append(promotion.getName());
        }
        return sb.toString();
    }

    private String formatCurrency(int price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormat.format(price) + PRICE_UNIT;
    }

    public boolean matchName(String name) {
        return this.name.equals(name);
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isPromotion() {
        return promotion != null;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public boolean isApplyPromotion(LocalDateTime localDateTime) {
        if (promotion == null) {
            return false;
        }
        return promotion.isApplyPromotion(localDateTime);
    }

    public boolean canBuy(int quantity) {
        return this.quantity >= quantity;
    }

    public boolean canGetForFreeByPromotion(int quantity) {
        if (promotion == null) {
            return false;
        }
        return this.promotion.canGetForFree(quantity) && this.quantity - quantity >= this.promotion.getGet();
    }

    public int getMaxCanGetForFreeByPromotion(int quantity) {
        if (promotion == null) {
            return 0;
        }
        if (quantity > this.quantity) {
            quantity = this.quantity;
        }
        return this.promotion.getMaxCanGetForFree(quantity);
    }

    public int getPromotionGet() {
        if (promotion == null) {
            return 0;
        }
        return this.promotion.getGet();
    }

    public int getMaxCanApplyPromotion(int quantity) {
        if (promotion == null) {
            return 0;
        }
        if (quantity > this.quantity) {
            quantity = this.quantity;
        }
        return this.promotion.getMaxCanApply(quantity);
    }

    public boolean InStock() {
        return quantity > 0;
    }

    public void decreaseQuantity(int amount) {
        quantity -= amount;
    }
}
