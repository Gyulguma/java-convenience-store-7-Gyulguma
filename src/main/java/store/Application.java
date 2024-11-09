package store;

import store.View.InputView;
import store.View.OutputView;
import store.controller.StoreController;
import store.service.FileService;
import store.service.StoreService;
import store.util.Converter;

public class Application {
    private final StoreController storeController;

    public Application() {
        Converter converter = new Converter();
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        FileService fileService = new FileService(converter);
        StoreService storeService = new StoreService(converter);
        this.storeController = new StoreController(inputView, outputView, fileService, storeService);
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    public void run() {
        this.storeController.run();
    }
}
