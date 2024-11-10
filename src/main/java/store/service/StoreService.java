package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.model.Item;
import store.model.Product;
import store.model.Products;
import store.model.PromotionStatus;
import store.model.Receipt;
import store.util.Converter;
import store.util.ServiceConstants;

public class StoreService {
    private final Converter converter;

    public StoreService(Converter converter) {
        this.converter = converter;
    }

    public List<Item> getItems(Products products, String input) {
        String[] inputs = input.split(ServiceConstants.ITEM_GROUP_SEPARATOR);
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
        item = item.replace(ServiceConstants.ITEM_GROUP_INDICATOR_START, "");
        item = item.replace(ServiceConstants.ITEM_GROUP_INDICATOR_END, "");
        return item.split(ServiceConstants.ITEM_ELEMENT_SEPARATOR);
    }

    private void validateItemName(Products products, String name) {
        if (!products.existProductByName(name)) {
            throw new IllegalArgumentException(ServiceConstants.ERROR_PRODUCT_NOT_FOUND);
        }
    }

    private void validateItemQuantity(Products products, String name, int quantity) {
        if (!products.canBuy(name, quantity)) {
            throw new IllegalArgumentException(ServiceConstants.ERROR_QUANTITY_OUT_OF_RANGE);
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

    public Receipt createReceipt(Products products, List<Item> items, boolean applyMembership) {
        int totalPrice = getTotalPrice(products, items);
        int discountByPromotion = getPromotionDiscount(products, items);
        int discountByMembership = 0;
        if (applyMembership) {
            discountByMembership = getMembershipDiscount(products, items);
        }
        int payment = totalPrice - discountByPromotion - discountByMembership;
        return new Receipt(items, totalPrice, discountByPromotion, discountByMembership, payment);
    }

    private int getTotalPrice(Products products, List<Item> items) {
        int totalPrice = 0;
        for (Item item : items) {
            totalPrice += getPrice(products, item);
        }
        return totalPrice;
    }

    private int getPrice(Products products, Item item) {
        Product product = products.findProductByName(item.getName());
        return item.getQuantity() * product.getPrice();
    }


    public int getPromotionDiscount(Products products, List<Item> items) {
        int discount = 0;
        for (Item item : items) {
            discount += getPriceForFreeByPromotion(products, item);
        }
        return discount;
    }

    private int getPriceForFreeByPromotion(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (product == null) {
            return 0;
        }
        int price = product.getPrice();
        int getFreeCount = product.getMaxCanGetForFreeByPromotion(item.getQuantity());
        return getFreeCount * price;
    }

    public int getMembershipDiscount(Products products, List<Item> items) {
        int totalPriceNotApplyPromotion = 0;
        for (Item item : items) {
            totalPriceNotApplyPromotion += getPriceNotApplyPromotion(products, item);
        }
        int discount = (int) (totalPriceNotApplyPromotion * 0.3);
        if (discount > 8000) {
            discount = 8000;
        }
        return discount;
    }

    private int getPriceNotApplyPromotion(Products products, Item item) {
        Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
        if (product == null) {
            product = products.findProductByNameAndPromotionIsNull(item.getName());
            return item.getQuantity() * product.getPrice();
        }
        int price = product.getPrice();
        int applyPromotion = product.getMaxCanApplyPromotion(item.getQuantity());
        int notApplyPromotionCount = item.getQuantity() - applyPromotion;
        return notApplyPromotionCount * price;
    }

    public List<Item> getItemsForFreeByPromotion(Products products, List<Item> items) {
        List<Item> freeItems = new ArrayList<>();
        for (Item item : items) {
            Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
            if (product == null) {
                continue;
            }
            int get = product.getMaxCanGetForFreeByPromotion(item.getQuantity());
            if (get == 0) {
                continue;
            }
            freeItems.add(new Item(item.getName(), get));
        }
        return freeItems;
    }

    public void processStock(Products products, List<Item> items) {
        for (Item item : items) {
            Product product = products.findProductByNameAndPromotionIsNotNull(item.getName());
            int quantity = item.getQuantity();
            if (product != null) {
                quantity = decreaseProductQuantity(product, quantity);
            }
            product = products.findProductByNameAndPromotionIsNull(item.getName());
            if (quantity == 0 || product == null) {
                continue;
            }
            product.decreaseQuantity(quantity);
        }
    }

    private int decreaseProductQuantity(Product product, int quantity) {
        if (product.canBuy(quantity)) {
            product.decreaseQuantity(quantity);
            return 0;
        }
        product.decreaseQuantity(product.getQuantity());
        return quantity - product.getQuantity();
    }

    public boolean isContinue(String input) {
        return input.equals("Y");
    }
}
