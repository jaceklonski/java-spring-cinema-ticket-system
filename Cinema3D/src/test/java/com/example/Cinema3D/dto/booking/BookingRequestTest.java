package com.example.Cinema3D.dto.booking;

import com.example.Cinema3D.dto.seat.SeatDto;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRequestTest {

    @Test
    void shouldSetAndGetFields() {
        BookingRequest request = new BookingRequest();
        Long screeningId = 42L;
        Set<SeatDto> seats = Set.of(
                new SeatDto(1, 2),
                new SeatDto(3, 4)
        );

        request.setScreeningId(screeningId);
        request.setSeats(seats);

        assertThat(request.getScreeningId()).isEqualTo(screeningId);
        assertThat(request.getSeats()).hasSize(2);
        assertThat(request.getSeats())
                .anyMatch(s -> s.getRow() == 1 && s.getNumber() == 2)
                .anyMatch(s -> s.getRow() == 3 && s.getNumber() == 4);
    }
}
