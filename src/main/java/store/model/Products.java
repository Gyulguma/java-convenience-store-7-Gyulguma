package store.model;

import java.util.ArrayList;
import java.util.List;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : products) {
            stringBuilder.append(product.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Product findProductByName(String name) {
        for (Product product : products) {
            if (product.matchName(name)) {
                return product;
            }
        }
        return null;
    }

    public boolean existProductByName(String name) {
        for (Product product : products) {
            if (product.matchName(name)) {
                return true;
            }
        }
        return false;
    }
}