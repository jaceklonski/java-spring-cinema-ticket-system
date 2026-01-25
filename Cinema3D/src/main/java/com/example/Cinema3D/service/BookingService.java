package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.booking.BookingRequest;
import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    @Transactional
    public Booking create(BookingRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new NotFoundException("Screening not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        Set<ScreeningSeat> screeningSeats = request.getSeats().stream()
                .map(dto -> {

                    Seat seat = seatRepository
                            .findByRoomAndRowAndNumber(
                                    screening.getRoom(),
                                    dto.getRow(),
                                    dto.getNumber()
                            )
                            .orElseThrow(() ->
                                    new NotFoundException("Seat not found")
                            );

                    ScreeningSeat ss = screeningSeatRepository
                            .findByScreeningIdAndSeatId(
                                    screening.getId(),
                                    seat.getId()
                            )
                            .orElseThrow(() ->
                                    new NotFoundException("Seat not assigned to screening")
                            );

                    if (ss.getStatus() != SeatStatus.RESERVED) {
                        throw new IllegalStateException("Seat not reserved");
                    }

                    ss.setStatus(SeatStatus.SOLD);
                    ss.setBooking(booking);

                    return ss;
                })
                .collect(Collectors.toSet());

        screeningSeatRepository.saveAll(screeningSeats);

        return booking;
    }

    @Transactional
    public Booking createFromCart(String username, List<CartItem> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Long screeningId = items.get(0).getScreeningId();

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException("Screening not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        for (CartItem item : items) {

            ScreeningSeat ss = screeningSeatRepository
                    .findByScreeningIdAndSeatRowAndSeatNumber(
                            screeningId,
                            item.getRow(),
                            item.getSeat()
                    )
                    .orElseThrow(() ->
                            new NotFoundException("Seat not found")
                    );

            if (ss.getStatus() != SeatStatus.RESERVED) {
                throw new IllegalStateException("Seat not reserved");
            }

            ss.setStatus(SeatStatus.SOLD);
            ss.setBooking(booking);

            screeningSeatRepository.save(ss);
        }

        return booking;
    }

    @Transactional(readOnly = true)
    public List<Booking> getMyBookings() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return bookingRepository.findByUserOrderByIdDesc(user);
    }
}
