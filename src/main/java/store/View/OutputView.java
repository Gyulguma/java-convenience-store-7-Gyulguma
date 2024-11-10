package store.View;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import store.model.Item;
import store.model.Product;
import store.model.Products;
import store.model.Receipt;
import store.util.constants.ViewConstants;

public class OutputView {
    public void printExceptionMessage(String message) {
        System.out.println(message);
    }

    public void printProducts(Products products) {
        String message = ViewConstants.WELCOME_MESSAGE + "\n" + ViewConstants.PRODUCT_INTRODUCE_MESSAGE + "\n\n"
                + products.toString();
        System.out.println(message);
    }

    public void printReceipt(Products products, Receipt receipt, List<Item> freeItems) {
        printReceiptHeader();
        int totalQuantity = printReceiptItems(products, receipt);
        printReceiptMiddle(freeItems);
        printReceiptBottom(receipt, totalQuantity);

    }

    private void printReceiptHeader() {
        System.out.println(ViewConstants.RECEIPT_HEADER);
        System.out.printf((ViewConstants.RECEIPT_HEADER_FORMAT) + "%n", ViewConstants.RECEIPT_PRODUCT_NAME,
                ViewConstants.RECEIPT_QUANTITY, ViewConstants.RECEIPT_AMOUNT);
    }

    private int printReceiptItems(Products products, Receipt receipt) {
        int totalQuantity = 0;
        List<Item> items = receipt.getItems();
        for (Item item : items) {
            Product product = products.findProductByName(item.getName());
            int price = product.getPrice();
            System.out.printf((ViewConstants.RECEIPT_FORMAT) + "%n", item.getName(), item.getQuantity(),
                    formatCurrency(price));
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    private void printReceiptMiddle(List<Item> freeItems) {
        if (freeItems.isEmpty()) {
            return;
        }
        System.out.println(ViewConstants.RECEIPT_MIDDLE);
        for (Item item : freeItems) {
            System.out.printf((ViewConstants.RECEIPT_FORMAT) + "%n", item.getName(), item.getQuantity(), "");
        }
    }

    private void printReceiptBottom(Receipt receipt, int totalQuantity) {
        String message = ViewConstants.RECEIPT_BOTTOM
                + String.format(ViewConstants.RECEIPT_FORMAT, ViewConstants.RECEIPT_TOTAL_PRICE, totalQuantity,
                formatCurrency(receipt.getTotalPrice())) + "\n"
                + String.format("%-10s %20s\n", ViewConstants.RECEIPT_PROMOTION_DISCOUNT,
                formatCurrency(-receipt.getDiscountByPromotion()))
                + String.format("%-10s %20s\n", ViewConstants.RECEIPT_MEMBERSHIP_DISCOUNT,
                formatCurrency(-receipt.getDiscountByMembership()))
                + String.format("%-10s %20s\n", ViewConstants.RECEIPT_PAYMENT, formatCurrency(receipt.getPayment()));
        System.out.println(message);
    }

    private String formatCurrency(int price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormat.format(price) + ViewConstants.PRICE_UNIT;
    }
}
