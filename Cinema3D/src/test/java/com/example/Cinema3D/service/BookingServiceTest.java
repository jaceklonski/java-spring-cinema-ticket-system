package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.booking.BookingRequest;
import com.example.Cinema3D.dto.seat.SeatDto;
import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock BookingRepository bookingRepository;
    @Mock ScreeningRepository screeningRepository;
    @Mock SeatRepository seatRepository;
    @Mock ScreeningSeatRepository screeningSeatRepository;
    @Mock UserRepository userRepository;

    @InjectMocks
    BookingService bookingService;

    @BeforeEach
    void setupSecurity() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("alice", "pw")
        );
    }

    // =====================================================
    // create(BookingRequest)
    // =====================================================

    @Test
    void create_success() {
        User user = new User(); user.setUsername("alice");

        CinemaRoom room = new CinemaRoom();
        Screening screening = new Screening();
        screening.setId(1L);
        screening.setRoom(room);

        Seat seat = new Seat(); seat.setId(10L);

        ScreeningSeat ss = new ScreeningSeat();
        ss.setStatus(SeatStatus.RESERVED);

        BookingRequest request = new BookingRequest();
        request.setScreeningId(1L);
        request.setSeats(Set.of(new SeatDto(1, 1)));

        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(user));
        when(screeningRepository.findById(1L))
                .thenReturn(Optional.of(screening));
        when(seatRepository.findByRoomAndRowAndNumber(room, 1, 1))
                .thenReturn(Optional.of(seat));
        when(screeningSeatRepository
                .findByScreeningIdAndSeatId(1L, 10L))
                .thenReturn(Optional.of(ss));

        Booking booking = bookingService.create(request);

        assertThat(booking).isNotNull();
        assertThat(ss.getStatus()).isEqualTo(SeatStatus.SOLD);

        verify(bookingRepository).save(any());
        verify(screeningSeatRepository).saveAll(any());
    }

    @Test
    void create_throwsWhenUserNotFound() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.empty());

        BookingRequest req = new BookingRequest();
        req.setScreeningId(1L);
        req.setSeats(Set.of(new SeatDto(1, 1)));

        assertThatThrownBy(() -> bookingService.create(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void create_throwsWhenSeatNotReserved() {
        User user = new User(); user.setUsername("alice");
        CinemaRoom room = new CinemaRoom();
        Screening screening = new Screening(); screening.setId(1L); screening.setRoom(room);
        Seat seat = new Seat(); seat.setId(10L);

        ScreeningSeat ss = new ScreeningSeat();
        ss.setStatus(SeatStatus.FREE);

        BookingRequest req = new BookingRequest();
        req.setScreeningId(1L);
        req.setSeats(Set.of(new SeatDto(1, 1)));

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        when(seatRepository.findByRoomAndRowAndNumber(room, 1, 1))
                .thenReturn(Optional.of(seat));
        when(screeningSeatRepository
                .findByScreeningIdAndSeatId(1L, 10L))
                .thenReturn(Optional.of(ss));

        assertThatThrownBy(() -> bookingService.create(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Seat not reserved");
    }

    // =====================================================
    // createFromCart
    // =====================================================

    @Test
    void createFromCart_success() {
        User user = new User(); user.setUsername("alice");
        Screening screening = new Screening(); screening.setId(1L);

        ScreeningSeat ss = new ScreeningSeat();
        ss.setStatus(SeatStatus.RESERVED);

        CartItem item = new CartItem();
        item.setScreeningId(1L);
        item.setRow(1);
        item.setSeat(1);

        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(user));
        when(screeningRepository.findById(1L))
                .thenReturn(Optional.of(screening));
        when(screeningSeatRepository
                .findByScreeningIdAndSeatRowAndSeatNumber(1L, 1, 1))
                .thenReturn(Optional.of(ss));

        Booking booking = bookingService.createFromCart(
                "alice",
                List.of(item)
        );

        assertThat(booking).isNotNull();
        assertThat(ss.getStatus()).isEqualTo(SeatStatus.SOLD);

        verify(bookingRepository).save(any());
        verify(screeningSeatRepository).save(ss);
    }

    @Test
    void createFromCart_throwsWhenCartEmpty() {
        assertThatThrownBy(() ->
                bookingService.createFromCart("alice", List.of())
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cart is empty");
    }

    @Test
    void createFromCart_throwsWhenSeatNotReserved() {
        User user = new User(); user.setUsername("alice");
        Screening screening = new Screening(); screening.setId(1L);

        ScreeningSeat ss = new ScreeningSeat();
        ss.setStatus(SeatStatus.FREE);

        CartItem item = new CartItem();
        item.setScreeningId(1L);
        item.setRow(1);
        item.setSeat(1);

        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(user));
        when(screeningRepository.findById(1L))
                .thenReturn(Optional.of(screening));
        when(screeningSeatRepository
                .findByScreeningIdAndSeatRowAndSeatNumber(1L, 1, 1))
                .thenReturn(Optional.of(ss));

        assertThatThrownBy(() ->
                bookingService.createFromCart("alice", List.of(item))
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Seat not reserved");
    }

    // =====================================================
    // getMyBookings
    // =====================================================

    @Test
    void getMyBookings_success() {
        User user = new User(); user.setUsername("alice");

        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findByUserOrderByIdDesc(user))
                .thenReturn(List.of(new Booking(), new Booking()));

        var result = bookingService.getMyBookings();

        assertThat(result).hasSize(2);
    }

    @Test
    void getMyBookings_throwsWhenUserNotFound() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingService.getMyBookings()
        )
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }
}
