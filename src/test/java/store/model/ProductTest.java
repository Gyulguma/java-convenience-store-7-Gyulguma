package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.util.Converter;

class ProductTest {
    Product product;
    Promotion promotion;

    @BeforeEach
    void setUp() {
        Converter converter = new Converter();
        LocalDateTime start = converter.toLocalDateTime("2024-01-01");
        LocalDateTime end = converter.toLocalDateTime("2024-12-31");
        promotion = new Promotion("탄산2+1", 2, 1, start, end);
        product = new Product("콜라", 1000, 10, promotion);
    }

    @Test
    void matchName() {
        String name = "콜라";
        assertTrue(product.matchName(name));
    }

    @Test
    void getPrice() {
        assertThat(product.getPrice()).isEqualTo(1000);
    }

    @Test
    void getQuantity() {
        assertThat(product.getQuantity()).isEqualTo(10);
    }

    @Test
    void isPromotion() {
        assertTrue(product.isPromotion());
    }

    @Test
    void getPromotion() {
        assertThat(product.getPromotion()).isEqualTo(promotion);
    }

    @Test
    void isApplyPromotion() {
        String value = "2024-02-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDate.parse(value, formatter).atStartOfDay();
        assertTrue(product.isApplyPromotion(now));
    }

    @Test
    void canBuy() {
        assertTrue(product.canBuy(5));
        assertThat(product.canBuy(100)).isFalse();
    }

    @Test
    void canGetForFreeByPromotion() {
        assertThat(product.canGetForFreeByPromotion(5)).isTrue();
        assertThat(product.canGetForFreeByPromotion(6)).isFalse();
    }

    @Test
    void getMaxCanGetForFreeByPromotion() {
        assertThat(product.getMaxCanGetForFreeByPromotion(5)).isEqualTo(1);
    }

    @Test
    void getPromotionGet() {
        assertThat(product.getPromotionGet()).isEqualTo(promotion.getGet());
    }

    @Test
    void getMaxCanApplyPromotion() {
        assertThat(product.getMaxCanApplyPromotion(5)).isEqualTo(3);
    }

    @Test
    void inStock() {
        assertThat(product.inStock()).isTrue();
    }

    @Test
    void decreaseQuantity() {
        product.decreaseQuantity(5);
        assertThat(product.getQuantity()).isEqualTo(5);
    }
}