package store.model;

import java.text.NumberFormat;
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

    public boolean canBuy(int quantity) {
        return this.quantity >= quantity;
    }
}
