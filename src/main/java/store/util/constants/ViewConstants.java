package store.util.constants;

public class ViewConstants {
    // inputView
    public static final String INPUT_ITEM_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    public static final String ITEM_INPUT_FORMAT = "^\\[(\\w|[가-힣]|.)+-\\d+\\](,\\[(\\w|[가-힣]|.)+-\\d+\\])*$";
    public static final String ERROR_INPUT_FORMAT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    public static final String INPUT_MEMBERSHIP_YN_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    public static final String INPUT_CONTINUE_YN_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    // outputView
    public static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    public static final String PRODUCT_INTRODUCE_MESSAGE = "현재 보유하고 있는 상품입니다.";
    public static final String RECEIPT_HEADER = "\n==============W 편의점================";
    public static final String RECEIPT_MIDDLE = "=============증\t정===============";
    public static final String RECEIPT_BOTTOM = "====================================\n";
    public static final String RECEIPT_HEADER_FORMAT = "%-10s %5s %15s";
    public static final String RECEIPT_FORMAT = "%-10s %5d %15s";
    public static final String RECEIPT_BOTTOM_FORMAT = "%-10s %20s\n";
    public static final String RECEIPT_PRODUCT_NAME = "상품명";
    public static final String RECEIPT_QUANTITY = "수량";
    public static final String RECEIPT_AMOUNT = "금액";
    public static final String RECEIPT_TOTAL_PRICE = "총구매액";
    public static final String RECEIPT_PROMOTION_DISCOUNT = "행사할인";
    public static final String RECEIPT_MEMBERSHIP_DISCOUNT = "멤버십할인";
    public static final String RECEIPT_PAYMENT = "내실돈";
}
