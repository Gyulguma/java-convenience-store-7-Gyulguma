package store.service;

import java.util.ArrayList;
import java.util.List;
import store.model.Item;
import store.model.Products;
import store.util.Converter;
import store.util.constants.ServiceConstants;

public class ItemService {
    private final Converter converter;

    public ItemService(Converter converter) {
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
}
