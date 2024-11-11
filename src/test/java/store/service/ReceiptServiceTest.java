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
import store.model.Receipt;
import store.util.Converter;

class ReceiptServiceTest {
    ItemService itemService;
    ReceiptService receiptService;
    Converter converter;
    Products products;
    List<Item> items;

    @BeforeEach
    void setUp() {
        converter = new Converter();
        itemService = new ItemService(converter);
        receiptService = new ReceiptService();
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

    @DisplayName("결제 금액을 계산하여 영수증을 반환한다")
    @Test
    void createReceipt() {
        Receipt receipt = this.receiptService.createReceipt(products, items, true);

        int total = 1000 * 2 + 1200 * 4 + 1200 * 10;
        assertThat(receipt.getTotalPrice()).isEqualTo(total);
        int discountPromotion = 1200 + 1200 * 2;
        int discountMembership = (int) ((2000 + 1200 + 4800) * 0.3);
        int payment = total - discountPromotion - discountMembership;
        assertThat(receipt.getPayment()).isEqualTo(payment);
    }

    @DisplayName("프로모션 할인 금액을 반환한다")
    @Test
    void getPromotionDiscount() {
        int discountPromotion = 1200 + 1200 * 2;

        assertThat(this.receiptService.getPromotionDiscount(products, items)).isEqualTo(discountPromotion);
    }

    @DisplayName("프로모션 적용 상품 제외한 금액에서 멤버십 할인 금액을 반환한다")
    @Test
    void getMembershipDiscount() {
        int discountMembership = (int) ((2000 + 1200 + 4800) * 0.3);

        assertThat(this.receiptService.getMembershipDiscount(products, items)).isEqualTo(discountMembership);
    }
}