package com.example.Cinema3D.dto.seat;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeatDtoTest {

    @Test
    void shouldCreateSeatDtoWithConstructor() {
        SeatDto dto = new SeatDto(2, 7);

        assertThat(dto.getRow()).isEqualTo(2);
        assertThat(dto.getNumber()).isEqualTo(7);
    }

    @Test
    void shouldSetAndGetValues() {
        SeatDto dto = new SeatDto();

        dto.setRow(3);
        dto.setNumber(9);

        assertThat(dto.getRow()).isEqualTo(3);
        assertThat(dto.getNumber()).isEqualTo(9);
    }
}
