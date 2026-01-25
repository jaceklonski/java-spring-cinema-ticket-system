package com.example.Cinema3D.dto.seat;

import com.example.Cinema3D.entity.SeatStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScreeningSeatDtoTest {

    @Test
    void shouldCreateDtoAndExposeFields() {
        ScreeningSeatDto dto =
                new ScreeningSeatDto(3, 12, SeatStatus.RESERVED);

        assertThat(dto.getRow()).isEqualTo(3);
        assertThat(dto.getNumber()).isEqualTo(12);
        assertThat(dto.getStatus()).isEqualTo(SeatStatus.RESERVED);
    }
}
