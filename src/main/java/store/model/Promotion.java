package store.model;

import java.time.LocalDateTime;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public boolean matchName(String name) {
        return name.equals(this.name);
    }

    public boolean isApplyPromotion(LocalDateTime localDateTime) {
        return !localDateTime.isBefore(startDate) && !localDateTime.isAfter(endDate);
    }

    public boolean canGetForFree(int quantity) {
        return quantity % (buy + get) == buy;
    }

    public int getMaxCanGetForFree(int quantity) {
        return quantity / (this.buy + this.get);
    }

    public int getMaxCanApply(int quantity) {
        return getMaxCanGetForFree(quantity) * (this.buy + this.get);
    }
}
