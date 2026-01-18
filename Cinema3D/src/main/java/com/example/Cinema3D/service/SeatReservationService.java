package com.example.Cinema3D.service;

import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatReservationService {

    private final ScreeningSeatRepository screeningSeatRepository;
    private final UserRepository userRepository;

    @Transactional
    public void reserveSeat(
            Long screeningId,
            int row,
            int number,
            String username
    ) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ScreeningSeat seat = screeningSeatRepository
                .lockSeat(screeningId, row, number)
                .orElseThrow(() -> new NotFoundException("Seat not found"));

        if (seat.getStatus() != SeatStatus.FREE) {
            throw new IllegalStateException("Seat already reserved");
        }

        seat.setStatus(SeatStatus.RESERVED);
        seat.setReservedAt(java.time.LocalDateTime.now());
        seat.setReservedBy(user);

        screeningSeatRepository.save(seat);
    }
}
