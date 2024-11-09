package store.View;

import store.model.Products;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_INTRODUCE_MESSAGE = "현재 보유하고 있는 상품입니다.";

    public void printExceptionMessage(String message) {
        System.out.println(message);
    }

    public void printProducts(Products products) {
        String stringBuilder = WELCOME_MESSAGE + "\n"
                + PRODUCT_INTRODUCE_MESSAGE + "\n\n"
                + products.toString();
        System.out.println(stringBuilder);
    }
}
