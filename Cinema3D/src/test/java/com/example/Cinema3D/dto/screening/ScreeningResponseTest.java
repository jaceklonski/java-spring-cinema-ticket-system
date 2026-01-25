package com.example.Cinema3D.dto.screening;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ScreeningResponseTest {

    @Test
    void shouldExposeAllFieldsFromConstructor() {
        LocalDateTime start = LocalDateTime.now();

        ScreeningResponse response = new ScreeningResponse(
                1L,
                start,
                10L,
                "Room A",
                20L,
                "Interstellar"
        );

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStartTime()).isEqualTo(start);
        assertThat(response.getRoomId()).isEqualTo(10L);
        assertThat(response.getRoomName()).isEqualTo("Room A");
        assertThat(response.getMovieId()).isEqualTo(20L);
        assertThat(response.getMovieTitle()).isEqualTo("Interstellar");
    }
}
