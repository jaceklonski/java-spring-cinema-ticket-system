package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.booking.BookingRequest;
import com.example.Cinema3D.entity.Booking;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.entity.Seat;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.BookingRepository;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;

    public Booking create(BookingRequest request) {

        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new NotFoundException("Screening not found"));

        Booking booking = new Booking();
        booking.setScreening(screening);
        booking.setCustomerName(request.getCustomerName());
        booking.setCreatedAt(LocalDateTime.now());

        Set<Seat> seats = request.getSeats().stream()
                .map(dto -> seatRepository
                        .findByRoomAndRowAndNumber(
                                screening.getRoom(),
                                dto.getRow(),
                                dto.getNumber()
                        )
                        .orElseThrow(() -> new NotFoundException(
                                "Seat not found: row=" + dto.getRow() +
                                        ", number=" + dto.getNumber()
                        ))
                )
                .collect(Collectors.toSet());

        booking.setSeats(seats);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> getByScreening(Long screeningId) {
        return bookingRepository.findByScreeningId(screeningId);
    }
}
