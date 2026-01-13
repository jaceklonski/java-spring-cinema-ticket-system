package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.booking.BookingRequest;
import com.example.Cinema3D.dto.booking.BookingResponse;
import com.example.Cinema3D.mapper.BookingMapper;
import com.example.Cinema3D.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponse create(@RequestBody @Valid BookingRequest request) {
        return BookingMapper.toResponse(
                bookingService.create(request)
        );
    }

    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.getAll()
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @GetMapping("/screening/{screeningId}")
    public List<BookingResponse> getByScreening(@PathVariable Long screeningId) {
        return bookingService.getByScreening(screeningId)
                .stream()
                .map(BookingMapper::toResponse)
                .toList();
    }
}
