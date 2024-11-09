package store.service;

import java.util.ArrayList;
import java.util.List;
import store.model.Item;
import store.model.Product;
import store.model.Products;
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
        Product product = products.findProductByName(name);
        validateItemQuantity(product, quantity);
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

    private void validateItemQuantity(Product product, int quantity) {
        if (!product.canBuy(quantity)) {
            throw new IllegalArgumentException(Constants.ERROR_QUANTITY_OUT_OF_RANGE);
        }
    }
}
