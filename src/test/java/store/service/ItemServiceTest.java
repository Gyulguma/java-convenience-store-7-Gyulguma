package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Item;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.util.Converter;

class ItemServiceTest {
    ItemService itemService;
    Converter converter;
    Products products;

    @BeforeEach
    void setUp() {
        converter = new Converter();
        itemService = new ItemService(converter);
        products = createProducts();
    }

    private Products createProducts() {
        List<Product> products = new ArrayList<>();
        LocalDateTime start = converter.toLocalDateTime("2024-01-01");
        LocalDateTime end = converter.toLocalDateTime("2024-12-31");
        Promotion promotion = new Promotion("탄산2+1", 2, 1, start, end);

        products.add(new Product("콜라", 1000, 10, promotion));
        products.add(new Product("콜라", 1000, 10, null));
        products.add(new Product("사이다", 1200, 8, promotion));
        products.add(new Product("사이다", 1200, 7, null));
        return new Products(products);
    }

    @DisplayName("상품이 존재하고, 재고가 있다면 정상적으로 Items을 생성한다")
    @Test
    void getItems() {
        String input = "[사이다-12],[콜라-2]";
        List<Item> items = itemService.getItems(products, input);

        assertThat(items.size()).isEqualTo(2);
        assertThat(items.get(0).getName()).isEqualTo("사이다");
        assertThat(items.get(0).getQuantity()).isEqualTo(12);
        assertThat(items.get(1).getName()).isEqualTo("콜라");
        assertThat(items.get(1).getQuantity()).isEqualTo(2);
    }

    @DisplayName("같은 상품을 입력하면 하나의 상품으로 Item을 생성한다")
    @Test
    void getItemsBySameName() {
        String input = "[사이다-5],[사이다-2],[사이다-3]";
        List<Item> items = itemService.getItems(products, input);

        assertThat(items.size()).isEqualTo(1);
        assertThat(items.get(0).getName()).isEqualTo("사이다");
        assertThat(items.get(0).getQuantity()).isEqualTo(10);
    }

    @DisplayName("상품이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void getItemsThrowByName() {
        String input = "[사이다-5],[탄산수-2]";

        assertThatThrownBy(() -> itemService.getItems(products, input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수량이 재고보다 많다면 경우 예외가 발생한다")
    @Test
    void getItemsThrowByQuantity() {
        String input = "[사이다-20],[탄산수-2]";

        assertThatThrownBy(() -> itemService.getItems(products, input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}