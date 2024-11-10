package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.model.Item;
import store.model.Product;
import store.model.Products;
import store.model.PromotionStatus;
import store.util.Constants;
import store.util.Converter;

public class StoreService {
    private final Converter converter;

    public StoreService(Converter converter) {
        this.converter = converter;
    }

    public List<Item> getItems(Products products, String input) {
        String[] inputs = input.split(Constants.ITEM_GROUP_SEPARATOR);
        List<Item> items = new ArrayList<>();
        for (String item : inputs) {
            items.add(createItem(products, item));
        }
        return items;
    }

    private Item createItem(Products products, String item) {
        String[] itemElements = parseItemElements(item);
        String name = itemElements[0];
        validateItemName(products, name);
        int quantity = this.converter.toInteger(itemElements[1]);
        validateItemQuantity(products, name, quantity);
        return new Item(name, quantity);
    }

    private String[] parseItemElements(String item) {
        item = item.replace(Constants.ITEM_GROUP_INDICATOR_START, "");
        item = item.replace(Constants.ITEM_GROUP_INDICATOR_END, "");
        return item.split(Constants.ITEM_ELEMENT_SEPARATOR);
    }

    private void validateItemName(Products products, String name) {
        if (!products.existProductByName(name)) {
            throw new IllegalArgumentException(Constants.ERROR_PRODUCT_NOT_FOUND);
        }
    }

    private void validateItemQuantity(Products products, String name, int quantity) {
        if (!products.canBuy(name, quantity)) {
            throw new IllegalArgumentException(Constants.ERROR_QUANTITY_OUT_OF_RANGE);
        }
    }

    public PromotionStatus checkPromotionState(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (product == null || !product.InStock() || !product.isApplyPromotion(DateTimes.now())) {
            return PromotionStatus.NO_PROMOTION;
        }
        if (product.canGetForFreeByPromotion(item.getQuantity())) {
            return PromotionStatus.NEED_GET;
        }
        if (!product.canBuy(item.getQuantity()) || !product.canBuy(item.getQuantity() + product.getPromotionGet())) {
            return PromotionStatus.NOT_ENOUGH_STOCK;
        }
        return PromotionStatus.NO_PROMOTION;
    }

    public String getPromotionMessage(Products products, Item item, PromotionStatus promotionStatus) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (promotionStatus == PromotionStatus.NOT_ENOUGH_STOCK) {
            int applyPromotion = product.getMaxCanApplyPromotion(item.getQuantity());
            int notApplyPromotionCount = item.getQuantity() - applyPromotion;
            return String.format(promotionStatus.getMessage(), item.getName(), notApplyPromotionCount);
        }
        if (promotionStatus == PromotionStatus.NEED_GET) {
            return String.format(promotionStatus.getMessage(), item.getName());
        }
        return promotionStatus.getMessage();
    }

    public void processItemByPromotion(Item item, PromotionStatus promotionStatus, String input, Products products) {
        if (promotionStatus == PromotionStatus.NEED_GET && input.equals("Y")) {
            addGetToItem(item, products);
            return;
        }
        if (promotionStatus == PromotionStatus.NOT_ENOUGH_STOCK && input.equals("N")) {
            takeOffFormItem(item, products);
            return;
        }
    }

    private void addGetToItem(Item item, Products products) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        int get = product.getPromotionGet();
        item.addQuantity(get);
    }

    private void takeOffFormItem(Item item, Products products) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        int applyPromotion = product.getMaxCanApplyPromotion(item.getQuantity());
        int notApplyPromotionCount = item.getQuantity() - applyPromotion;
        item.takeOffQuantity(notApplyPromotionCount);
    }
}
