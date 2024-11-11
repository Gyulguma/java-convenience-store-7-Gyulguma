package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.model.Item;
import store.model.Product;
import store.model.Products;
import store.model.PromotionApplyStatus;
import store.util.constants.ServiceConstants;

public class StoreService {

    public StoreService() {
    }

    public PromotionApplyStatus checkPromotionState(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (product == null || !product.inStock() || !product.isApplyPromotion(DateTimes.now())) {
            return PromotionApplyStatus.NO_PROMOTION;
        }
        if (product.canGetForFreeByPromotion(item.getQuantity())) {
            return PromotionApplyStatus.NEED_GET;
        }
        if (!product.canBuy(item.getQuantity()) || !product.canBuy(item.getQuantity() + product.getPromotionGet())) {
            return PromotionApplyStatus.NOT_ENOUGH_STOCK;
        }
        return PromotionApplyStatus.NO_PROMOTION;
    }

    public String getPromotionMessage(Products products, Item item, PromotionApplyStatus promotionApplyStatus) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (promotionApplyStatus == PromotionApplyStatus.NOT_ENOUGH_STOCK) {
            int applyPromotion = product.getMaxCanApplyPromotion(item.getQuantity());
            int notApplyPromotionCount = item.getQuantity() - applyPromotion;
            return String.format(promotionApplyStatus.getMessage(), item.getName(), notApplyPromotionCount);
        }
        if (promotionApplyStatus == PromotionApplyStatus.NEED_GET) {
            return String.format(promotionApplyStatus.getMessage(), item.getName());
        }
        return promotionApplyStatus.getMessage();
    }

    public void processQuantityByPromotion(Item item, PromotionApplyStatus promotionApplyStatus, String input,
                                           Products products) {
        if (promotionApplyStatus == PromotionApplyStatus.NEED_GET && input.equals(ServiceConstants.YES)) {
            addGetToItem(item, products);
            return;
        }
        if (promotionApplyStatus == PromotionApplyStatus.NOT_ENOUGH_STOCK && input.equals(ServiceConstants.NO)) {
            takeOffFormItem(item, products);
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

    public List<Item> getItemsForFreeByPromotion(Products products, List<Item> items) {
        List<Item> freeItems = new ArrayList<>();
        for (Item item : items) {
            int getForFree = getGetCountByPromotion(products, item);
            if (getForFree == 0) {
                continue;
            }
            freeItems.add(new Item(item.getName(), getForFree));
        }
        return freeItems;
    }

    private int getGetCountByPromotion(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (product == null || !product.isApplyPromotion(DateTimes.now())) {
            return 0;
        }
        return product.getMaxCanGetForFreeByPromotion(item.getQuantity());
    }

    public void processStock(Products products, List<Item> items) {
        for (Item item : items) {
            int quantity = processPromotionProduct(products, item);
            processNotPromotionProduct(products, item, quantity);
        }
    }

    private int processPromotionProduct(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        int quantity = item.getQuantity();
        if (product != null) {
            quantity = decreaseProductQuantity(product, quantity);
        }
        return quantity;
    }

    private void processNotPromotionProduct(Products products, Item item, int quantity) {
        Product product = products.findProductByNameAndPromotionIsNull(item.getName());
        if (quantity == 0 || product == null) {
            return;
        }
        product.decreaseQuantity(quantity);
    }

    private int decreaseProductQuantity(Product product, int quantity) {
        if (product.canBuy(quantity)) {
            product.decreaseQuantity(quantity);
            return 0;
        }
        int remainingQuantity = quantity - product.getQuantity();
        product.decreaseQuantity(product.getQuantity());
        return remainingQuantity;
    }

    public boolean isContinue(String input) {
        return input.equals(ServiceConstants.YES);
    }
}
