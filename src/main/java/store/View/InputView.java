package store.View;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String INPUT_ITEM_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String ITEM_INPUT_FORMAT = "^\\[(\\w|[가-힣])+-\\d+\\](,\\[(\\w|[가-힣])+-\\d+\\])*$";
    private static final String ERROR_INPUT_FORMAT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

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
}
