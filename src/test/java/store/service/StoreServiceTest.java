package store.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import store.model.PromotionApplyStatus;
import store.util.Converter;

class StoreServiceTest {
    ItemService itemService;
    StoreService storeService;
    Converter converter;
    Products products;
    List<Item> items;

    @BeforeEach
    void setUp() {
        converter = new Converter();
        itemService = new ItemService(converter);
        storeService = new StoreService();
        products = createProducts();
        items = createItems();
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
        products.add(new Product("탄산수", 1200, 6, promotion));
        products.add(new Product("탄산수", 1200, 6, null));
        return new Products(products);
    }

    private List<Item> createItems() {
        String input = "[콜라-2],[탄산수-4],[사이다-10]";
        return itemService.getItems(products, input);
    }

    @DisplayName("item의 프로모션 적용 상태를 반환한다")
    @Test
    void checkPromotionState() {
        assertThat(this.storeService.checkPromotionState(products, items.get(0))).isEqualTo(
                PromotionApplyStatus.NEED_GET);
        assertThat(this.storeService.checkPromotionState(products, items.get(1))).isEqualTo(
                PromotionApplyStatus.NO_PROMOTION);
        assertThat(this.storeService.checkPromotionState(products, items.get(2))).isEqualTo(
                PromotionApplyStatus.NOT_ENOUGH_STOCK);
    }

    @DisplayName("프로모션 상태에 따라 안내 메시지를 반환한다")
    @Test
    void getPromotionMessage() {
        assertThat(this.storeService.getPromotionMessage(products, items.get(0), PromotionApplyStatus.NEED_GET))
                .contains("현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        assertThat(this.storeService.getPromotionMessage(products, items.get(2), PromotionApplyStatus.NOT_ENOUGH_STOCK))
                .contains("현재 사이다 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }

    @DisplayName("프로모션 상태와 사용자 입력에 따라 item의 수량을 수정한다")
    @Test
    void processQuantityByPromotion() {
        this.storeService.processQuantityByPromotion(items.get(0), PromotionApplyStatus.NEED_GET, "Y", products);
        this.storeService.processQuantityByPromotion(items.get(2), PromotionApplyStatus.NOT_ENOUGH_STOCK, "N",
                products);

        assertThat(items.get(0).getQuantity()).isEqualTo(3);
        assertThat(items.get(1).getQuantity()).isEqualTo(4);
        assertThat(items.get(2).getQuantity()).isEqualTo(6);
    }

    @DisplayName("증정품 목록을 반환한다")
    @Test
    void getItemsForFreeByPromotion() {
        List<Item> freeItems = this.storeService.getItemsForFreeByPromotion(products, items);

        assertThat(freeItems.size()).isEqualTo(2);
        assertThat(freeItems.get(0).getName() + freeItems.get(0).getQuantity()).isEqualTo("탄산수1");
        assertThat(freeItems.get(1).getName() + freeItems.get(1).getQuantity()).isEqualTo("사이다2");
    }

    @DisplayName("구매한 상품의 수량만큼 재고를 차감한다")
    @Test
    void processStock() {
        this.storeService.processStock(products, items);
        List<Product> processProduct = products.getProducts();

        assertThat(processProduct.get(0).getQuantity()).isEqualTo(8);
        assertThat(processProduct.get(1).getQuantity()).isEqualTo(10);
        assertThat(processProduct.get(2).getQuantity()).isEqualTo(0);
        assertThat(processProduct.get(3).getQuantity()).isEqualTo(5);
        assertThat(processProduct.get(4).getQuantity()).isEqualTo(2);
        assertThat(processProduct.get(5).getQuantity()).isEqualTo(6);
    }
}