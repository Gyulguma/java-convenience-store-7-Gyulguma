package store.View;

import java.util.List;
import store.model.Product;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_INTRODUCE_MESSAGE = "현재 보유하고 있는 상품입니다.";

    public void printExceptionMessage(String message) {
        System.out.println(message);
    }

    public void printProducts(List<Product> products) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WELCOME_MESSAGE);
        stringBuilder.append("\n");
        stringBuilder.append(PRODUCT_INTRODUCE_MESSAGE);
        stringBuilder.append("\n\n");
        for (Product product : products) {
            stringBuilder.append(product.toString());
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder);
    }
}
