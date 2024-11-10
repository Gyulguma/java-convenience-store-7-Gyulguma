package store.controller;

import java.io.FileNotFoundException;
import java.util.List;
import store.View.InputView;
import store.View.OutputView;
import store.model.Item;
import store.model.Products;
import store.model.PromotionStatus;
import store.model.Promotions;
import store.service.FileService;
import store.service.StoreService;

public class StoreController {
    private static final String PRODUCTS_FILE_PATH = "/products.md";
    private static final String PROMOTIONS_FILE_PATH = "/promotions.md";

    private final InputView inputView;
    private final OutputView outputView;
    private final FileService fileService;
    private final StoreService storeService;

    public StoreController(InputView inputView, OutputView outputView, FileService fileService,
                           StoreService storeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.fileService = fileService;
        this.storeService = storeService;
    }

    public void run() {
        Products products = printProducts();
        List<Item> items = createItem(products);
        List<Item> processItems = processItemsByPromotion(products, items);
        int discountByMembership = applyMembership(products, processItems);
    }

    private Products printProducts() {
        Promotions promotions = null;
        Products products = null;
        try {
            promotions = this.fileService.getPromotions(PROMOTIONS_FILE_PATH);
            products = this.fileService.getProducts(PRODUCTS_FILE_PATH, promotions);
            this.outputView.printProducts(products);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            this.outputView.printExceptionMessage(e.getMessage());
        }
        return products;
    }

    private List<Item> createItem(Products products) {
        while (true) {
            try {
                String input = this.inputView.readItem();
                return this.storeService.getItems(products, input);
            } catch (IllegalArgumentException e) {
                this.outputView.printExceptionMessage(e.getMessage());
            }
        }
    }

    private List<Item> processItemsByPromotion(Products products, List<Item> items) {
        for (Item item : items) {
            PromotionStatus promotionStatus = this.storeService.checkPromotionState(products, item);
            if (promotionStatus == PromotionStatus.NO_PROMOTION) {
                continue;
            }
            String promotionsMessage = this.storeService.getPromotionMessage(products, item, promotionStatus);
            String input = this.inputView.readYN(promotionsMessage);
            this.storeService.processItemByPromotion(item, promotionStatus, input, products);
        }
        return items;
    }

    private int applyMembership(Products products, List<Item> items) {
        String input = this.inputView.readMembershipYN();
        if (input.equals("Y")) {
            return this.storeService.getMembershipDiscount(products, items);
        }
        return 0;
    }
}
