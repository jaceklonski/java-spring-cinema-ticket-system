package com.example.Cinema3D.scheduler;

import com.example.Cinema3D.entity.ScreeningSeat;
import com.example.Cinema3D.entity.SeatStatus;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatReservationCleanupJob {

    private final ScreeningSeatRepository screeningSeatRepository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void releaseExpiredReservations() {

        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(15);

        List<ScreeningSeat> expired =
                screeningSeatRepository.findExpiredReservations(
                        SeatStatus.RESERVED,
                        expirationTime
                );

        for (ScreeningSeat seat : expired) {
            seat.setStatus(SeatStatus.FREE);
            seat.setReservedAt(null);
            seat.setReservedBy(null);

        }
    }
}
