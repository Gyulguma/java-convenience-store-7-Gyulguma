package store.model;

public enum PromotionApplyStatus {
    NO_PROMOTION(""),
    NEED_GET("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    NOT_ENOUGH_STOCK("현재 %s %s개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");

    private final String message;

    PromotionApplyStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
