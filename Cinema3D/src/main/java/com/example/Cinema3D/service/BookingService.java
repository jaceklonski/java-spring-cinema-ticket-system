package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.booking.BookingRequest;
import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ScreeningSeatRepository screeningSeatRepository;

    /**
     * Tworzenie rezerwacji (sprzedaż biletów)
     */
    @Transactional
    public Booking create(BookingRequest request) {

        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new NotFoundException("Screening not found"));

        Booking booking = new Booking();
        booking.setScreening(screening);
        booking.setCustomerName(request.getCustomerName());
        booking.setCreatedAt(LocalDateTime.now());

        Set<ScreeningSeat> screeningSeats = request.getSeats().stream()
                .map(dto -> {
                    // 1. Znajdź fizyczne miejsce w sali
                    Seat seat = seatRepository
                            .findByRoomAndRowAndNumber(
                                    screening.getRoom(),
                                    dto.getRow(),
                                    dto.getNumber()
                            )
                            .orElseThrow(() ->
                                    new NotFoundException(
                                            "Seat not found: row=" + dto.getRow()
                                                    + ", number=" + dto.getNumber()
                                    ));

                    // 2. Znajdź miejsce przypisane do seansu
                    ScreeningSeat screeningSeat = screeningSeatRepository
                            .findByScreeningIdAndSeatId(
                                    screening.getId(),
                                    seat.getId()
                            )
                            .orElseThrow(() ->
                                    new NotFoundException("Seat not assigned to screening"));

                    // 3. Sprawdź dostępność
                    if (screeningSeat.getStatus() != SeatStatus.FREE) {
                        throw new IllegalStateException(
                                "Seat already taken: row=" + dto.getRow()
                                        + ", number=" + dto.getNumber()
                        );
                    }

                    // 4. Oznacz jako sprzedane
                    screeningSeat.setStatus(SeatStatus.SOLD);
                    screeningSeat.setBooking(booking);

                    return screeningSeat;
                })
                .collect(Collectors.toSet());

        // zapis rezerwacji
        bookingRepository.save(booking);
        screeningSeatRepository.saveAll(screeningSeats);

        return booking;
    }

    /**
     * Pobranie wszystkich rezerwacji
     */
    @Transactional(readOnly = true)
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    /**
     * Pobranie rezerwacji dla konkretnego seansu
     */
    @Transactional(readOnly = true)
    public List<Booking> getByScreening(Long screeningId) {
        return bookingRepository.findByScreeningId(screeningId);
    }
}
