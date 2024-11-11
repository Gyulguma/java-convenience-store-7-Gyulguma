package store.service;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.Promotions;
import store.util.Converter;
import store.util.FileReader;
import store.util.constants.ServiceConstants;

public class FileService {
    private final FileReader fileReader;
    private final Converter converter;

    public FileService(Converter converter) {
        this.fileReader = new FileReader();
        this.converter = converter;
    }

    public Promotions getPromotions(String filePath) throws FileNotFoundException {
        List<String> fileLines = fileReader.readFile(filePath);
        List<Promotion> promotions = new ArrayList<>();
        for (int i = 1; i < fileLines.size(); i++) {
            String line = fileLines.get(i);
            String[] promotionLine = line.split(ServiceConstants.ITEM_GROUP_SEPARATOR);
            Promotion promotion = createPromotion(promotionLine);
            promotions.add(promotion);
        }
        return new Promotions(promotions);
    }

    private Promotion createPromotion(String[] promotionLine) {
        String promotionName = promotionLine[0];
        int buy = this.converter.toInteger(promotionLine[1]);
        int get = this.converter.toInteger(promotionLine[2]);
        LocalDateTime startDate = this.converter.toLocalDateTime(promotionLine[3]);
        LocalDateTime endDate = this.converter.toLocalDateTime(promotionLine[4]);
        return new Promotion(promotionName, buy, get, startDate, endDate);
    }

    public Products getProducts(String filePath, Promotions promotions) throws FileNotFoundException {
        List<String> fileLines = fileReader.readFile(filePath);
        List<Product> products = new ArrayList<>();
        Product preProduct = null;
        for (int i = 1; i < fileLines.size(); i++) {
            String line = fileLines.get(i);
            String[] productLine = line.split(ServiceConstants.ITEM_GROUP_SEPARATOR);
            Product product = createProduct(productLine, promotions);
            checkAddProduct(products, preProduct, product);
            products.add(product);
            preProduct = product;
        }
        return new Products(products);
    }

    private void checkAddProduct(List<Product> products, Product preProduct, Product product) {
        if (preProduct != null && !preProduct.getName().equals(product.getName())
                && preProduct.getPromotion() != null) {
            Product newProduct =
                    new Product(preProduct.getName(), preProduct.getPrice(), 0, null);
            products.add(newProduct);
        }
    }

    private Product createProduct(String[] productLine, Promotions promotions) {
        String productName = productLine[0];
        int productPrice = this.converter.toInteger(productLine[1]);
        int quantity = this.converter.toInteger(productLine[2]);
        Promotion promotion = promotions.findPromotionByName(productLine[3]);
        return new Product(productName, productPrice, quantity, promotion);
    }
}
