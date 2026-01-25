package com.example.Cinema3D.dto.cart;

import com.example.Cinema3D.entity.TicketType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    @Test
    void shouldCreateCartItemAndExposeFields() {
        CartItem item = new CartItem(5, 10, TicketType.NORMAL);

        assertThat(item.getRow()).isEqualTo(5);
        assertThat(item.getSeat()).isEqualTo(10);
        assertThat(item.getTicketType()).isEqualTo(TicketType.NORMAL);
    }
}
