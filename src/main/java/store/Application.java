package store;

import store.View.InputView;
import store.View.OutputView;
import store.controller.StoreController;
import store.service.FileService;

public class Application {
    private final StoreController storeController;

    public Application() {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        FileService fileService = new FileService();
        this.storeController = new StoreController(inputView, outputView, fileService);
    }

    public void run() {
        this.storeController.run();
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}
