package com.example.Cinema3D.dto.booking;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingResponseTest {

    @Test
    void shouldReturnValuesFromConstructor() {
        Long id = 1L;
        Long screeningId = 10L;
        String movieTitle = "Interstellar";
        int seatsCount = 3;
        LocalDateTime createdAt = LocalDateTime.now();

        BookingResponse response = new BookingResponse(
                id,
                screeningId,
                movieTitle,
                seatsCount,
                createdAt
        );
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getScreeningId()).isEqualTo(screeningId);
        assertThat(response.getMovieTitle()).isEqualTo(movieTitle);
        assertThat(response.getSeatsCount()).isEqualTo(seatsCount);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }
}
