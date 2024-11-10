package store.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemTest {
    Item item;

    @BeforeEach
    void setUp() {
        item = new Item("콜라", 10);
    }

    @Test
    void testEquals() {
        Item newItem = new Item("콜라", 20);

        assertThat(item.equals(newItem)).isTrue();
    }

    @Test
    void getName() {
        assertThat(item.getName()).isEqualTo("콜라");
    }

    @Test
    void getQuantity() {
        assertThat(item.getQuantity()).isEqualTo(10);
    }

    @Test
    void addQuantity() {
        item.addQuantity(5);
        assertThat(item.getQuantity()).isEqualTo(15);
    }

    @Test
    void takeOffQuantity() {
        item.takeOffQuantity(5);
        assertThat(item.getQuantity()).isEqualTo(5);
    }
}