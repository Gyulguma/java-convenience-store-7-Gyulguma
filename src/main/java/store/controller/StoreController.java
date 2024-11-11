package store.controller;

import java.io.FileNotFoundException;
import java.util.List;
import store.View.InputView;
import store.View.OutputView;
import store.model.Item;
import store.model.Products;
import store.model.PromotionApplyStatus;
import store.model.Promotions;
import store.model.Receipt;
import store.service.FileService;
import store.service.ItemService;
import store.service.ReceiptService;
import store.service.StoreService;
import store.util.constants.ServiceConstants;

public class StoreController {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";

    private final InputView inputView;
    private final OutputView outputView;
    private final FileService fileService;
    private final ItemService itemService;
    private final ReceiptService receiptService;
    private final StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView, FileService fileService,
                           ItemService itemService, ReceiptService receiptService, StoreService storeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.fileService = fileService;
        this.itemService = itemService;
        this.receiptService = receiptService;
        this.storeService = storeService;
    }

    public void run() {
        Products products = createProducts();
        if (products == null) {
            return;
        }
        boolean operate = true;
        while (operate) {
            operate = operate(products);
        }
    }

    private boolean operate(Products products) {
        printProducts(products);
        List<Item> items = createItem(products);
        List<Item> processedItems = processItemsByPromotion(products, items);
        boolean applyMembership = applyMembership();
        Receipt receipt = createReceipt(products, processedItems, applyMembership);
        printReceipt(products, receipt);
        processStock(products, processedItems);
        return isContinue();
    }

    private Products createProducts() {
        Promotions promotions;
        Products products = null;
        try {
            promotions = this.fileService.getPromotions(PROMOTIONS_FILE_PATH);
            products = this.fileService.getProducts(PRODUCTS_FILE_PATH, promotions);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            this.outputView.printExceptionMessage(e.getMessage());
        }
        return products;
    }

    private void printProducts(Products products) {
        this.outputView.printProducts(products);
    }

    private List<Item> createItem(Products products) {
        while (true) {
            try {
                String input = this.inputView.readItem();
                return this.itemService.getItems(products, input);
            } catch (IllegalArgumentException e) {
                this.outputView.printExceptionMessage(e.getMessage());
            }
        }
    }

    private List<Item> processItemsByPromotion(Products products, List<Item> items) {
        for (Item item : items) {
            PromotionApplyStatus promotionApplyStatus = this.storeService.checkPromotionState(products, item);
            if (promotionApplyStatus == PromotionApplyStatus.NO_PROMOTION) {
                continue;
            }
            String promotionsMessage = this.storeService.getPromotionMessage(products, item, promotionApplyStatus);
            String input = this.inputView.readYN(promotionsMessage);
            this.storeService.processQuantityByPromotion(item, promotionApplyStatus, input, products);
        }
        return items;
    }

    private boolean applyMembership() {
        String input = this.inputView.readMembershipYN();
        return input.equals(ServiceConstants.YES);
    }

    private Receipt createReceipt(Products products, List<Item> items, boolean applyMembership) {
        return this.receiptService.createReceipt(products, items, applyMembership);
    }

    private void printReceipt(Products products, Receipt receipt) {
        List<Item> freeItems = this.storeService.getItemsForFreeByPromotion(products, receipt.getItems());
        this.outputView.printReceipt(products, receipt, freeItems);
    }

    private void processStock(Products products, List<Item> items) {
        this.storeService.processStock(products, items);
    }

    private boolean isContinue() {
        String input = this.inputView.readContinue();
        return this.storeService.isContinue(input);
    }
}
