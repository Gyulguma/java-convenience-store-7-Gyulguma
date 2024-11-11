package store.View;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String INPUT_ITEM_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String ITEM_INPUT_FORMAT = "^\\[.+-\\d+\\](,\\[.+-\\d+\\])*$";
    private static final String ERROR_INPUT_FORMAT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String INPUT_MEMBERSHIP_YN_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String INPUT_CONTINUE_YN_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String readItem() {
        System.out.println(INPUT_ITEM_MESSAGE);
        String input = Console.readLine();
        validateItemInputFormat(input);
        return input;
    }

    private void validateItemInputFormat(String input) {
        if (!input.matches(ITEM_INPUT_FORMAT)) {
            throw new IllegalArgumentException(ERROR_INPUT_FORMAT);
        }
    }

    public String readMembershipYN() {
        return readYN(INPUT_MEMBERSHIP_YN_MESSAGE);
    }

    public String readYN(String promotionsMessage) {
        while (true) {
            try {
                System.out.println(promotionsMessage);
                String input = Console.readLine();
                validateYNInput(input);
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateYNInput(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException(ERROR_INPUT_FORMAT);
        }
    }

    public String readContinue() {
        return readYN(INPUT_CONTINUE_YN_MESSAGE);
    }
}
