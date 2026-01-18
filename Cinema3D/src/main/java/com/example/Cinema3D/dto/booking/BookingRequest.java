package com.example.Cinema3D.dto.booking;

import com.example.Cinema3D.dto.seat.SeatDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class BookingRequest {

    @NotNull
    private Long screeningId;

    @NotEmpty
    private Set<SeatDto> seats;

    public Long getScreeningId() {
        return screeningId;
    }

    public Set<SeatDto> getSeats() {
        return seats;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
    }

    public void setSeats(Set<SeatDto> seats) {
        this.seats = seats;
    }
}
