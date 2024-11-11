package store.View;

import camp.nextstep.edu.missionutils.Console;
import store.util.constants.ViewConstants;

public class InputView {
    public String readItem() {
        System.out.println(ViewConstants.INPUT_ITEM_MESSAGE);
        String input = Console.readLine();
        validateItemInputFormat(input);
        return input;
    }

    private void validateItemInputFormat(String input) {
        if (!input.matches(ViewConstants.ITEM_INPUT_FORMAT)) {
            throw new IllegalArgumentException(ViewConstants.ERROR_INPUT_FORMAT);
        }
    }

    public String readMembershipYN() {
        return readYN(ViewConstants.INPUT_MEMBERSHIP_YN_MESSAGE);
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
            throw new IllegalArgumentException(ViewConstants.ERROR_INPUT_FORMAT);
        }
    }

    public String readContinue() {
        return readYN(ViewConstants.INPUT_CONTINUE_YN_MESSAGE);
    }
}
