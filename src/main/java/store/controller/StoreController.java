package store.controller;

import java.io.FileNotFoundException;
import java.util.List;
import store.View.InputView;
import store.View.OutputView;
import store.model.Product;
import store.model.Promotions;
import store.service.FileService;

public class StoreController {
    private static final String PRODUCTS_FILE_PATH = "/products.md";
    private static final String PROMOTIONS_FILE_PATH = "/promotions.md";

    private final InputView inputView;
    private final OutputView outputView;
    private final FileService fileService;

    public StoreController(InputView inputView, OutputView outputView, FileService fileService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.fileService = fileService;
    }

    public void run() {
        try {
            Promotions promotions = this.fileService.getPromotions(PROMOTIONS_FILE_PATH);
            List<Product> products = this.fileService.getProducts(PRODUCTS_FILE_PATH, promotions);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            this.outputView.printExceptionMessage(e.getMessage());
        }
    }
}
