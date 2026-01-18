package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.booking.BookingRequest;
import com.example.Cinema3D.dto.seat.SeatDto;
import com.example.Cinema3D.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookingViewController {

    private final BookingService bookingService;

    @PostMapping("/cinema/booking")
    public String book(
            @RequestParam Long screeningId,
            @RequestParam String seats
    ) {

        Set<SeatDto> seatDtos = Arrays.stream(seats.split(","))
                .map(s -> s.split("-"))
                .map(p -> new SeatDto(
                        Integer.parseInt(p[0]),
                        Integer.parseInt(p[1])
                ))
                .collect(Collectors.toSet());

        BookingRequest request = new BookingRequest();
        request.setScreeningId(screeningId);
        request.setSeats(seatDtos);

        bookingService.create(request);

        return "redirect:/my-bookings";
    }
}
