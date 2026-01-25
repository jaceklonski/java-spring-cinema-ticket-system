package com.example.Cinema3D.service;

import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatReservationServiceTest {

    @Mock
    ScreeningSeatRepository screeningSeatRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    SeatReservationService seatReservationService;

    @Test
    void reserveSeat_successfullyReservesSeat() {
        User user = new User();
        user.setUsername("john");

        ScreeningSeat seat = new ScreeningSeat();
        seat.setStatus(SeatStatus.FREE);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));
        when(screeningSeatRepository.lockSeat(1L, 1, 1))
                .thenReturn(Optional.of(seat));

        seatReservationService.reserveSeat(1L, 1, 1, "john");

        assertEquals(SeatStatus.RESERVED, seat.getStatus());
        assertNotNull(seat.getReservedAt());
        assertEquals(user, seat.getReservedBy());

        verify(screeningSeatRepository).save(seat);
    }

    @Test
    void reserveSeat_throwsWhenUserNotFound() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> seatReservationService.reserveSeat(1L, 1, 1, "unknown")
        );

        verifyNoInteractions(screeningSeatRepository);
    }


    @Test
    void reserveSeat_throwsWhenSeatNotFound() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));
        when(screeningSeatRepository.lockSeat(1L, 1, 1))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> seatReservationService.reserveSeat(1L, 1, 1, "john")
        );
    }


    @Test
    void reserveSeat_throwsWhenSeatAlreadyReserved() {
        User user = new User();
        user.setUsername("john");

        ScreeningSeat seat = new ScreeningSeat();
        seat.setStatus(SeatStatus.RESERVED);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));
        when(screeningSeatRepository.lockSeat(1L, 1, 1))
                .thenReturn(Optional.of(seat));

        assertThrows(
                IllegalStateException.class,
                () -> seatReservationService.reserveSeat(1L, 1, 1, "john")
        );

        verify(screeningSeatRepository, never()).save(any());
    }
}
