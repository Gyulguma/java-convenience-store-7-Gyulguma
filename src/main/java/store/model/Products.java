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

    public Products findAllProductByName(String name) {
        List<Product> foundProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.matchName(name)) {
                foundProducts.add(product);
            }
        }
        return new Products(foundProducts);
    }

    public boolean existProductByName(String name) {
        for (Product product : products) {
            if (product.matchName(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean canBuy(String name, int quantity) {
        Products foundProducts = findAllProductByName(name);
        int totalStock = 0;
        for (Product product : foundProducts.products) {
            totalStock += product.getQuantity();
        }
        return totalStock >= quantity;
    }

    public Product findProductByNameAndPromotionIsNotNull(String name) {
        Products foundProducts = findAllProductByName(name);
        for (Product product : foundProducts.products) {
            if (product.isPromotion()) {
                return product;
            }
        }
        return null;
    }

    public Product findProductByNameAndPromotionIsNull(String name) {
        Products foundProducts = findAllProductByName(name);
        for (Product product : foundProducts.products) {
            if (!product.isPromotion()) {
                return product;
            }
        }
        return null;
    }

    public Product findProductByName(String name) {
        for (Product product : products) {
            if (product.matchName(name)) {
                return product;
            }
        }
        return null;
    }
}