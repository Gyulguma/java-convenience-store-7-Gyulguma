package store.model;

import java.time.LocalDateTime;

public class Promotion {
    private String name;
    private int byu;
    private int get;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Promotion(String name, int byu, int get, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.byu = byu;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean matchName(String name) {
        return name.equals(this.name);
    }

    public String getName() {
        return name;
    }
}
