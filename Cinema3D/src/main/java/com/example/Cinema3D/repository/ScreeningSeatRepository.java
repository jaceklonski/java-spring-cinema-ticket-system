package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.ScreeningSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScreeningSeatRepository
        extends JpaRepository<ScreeningSeat, Long> {

    List<ScreeningSeat> findByScreeningId(Long screeningId);

    Optional<ScreeningSeat> findByScreeningIdAndSeatId(
            Long screeningId,
            Long seatId
    );

    @Query("""
        select ss
        from ScreeningSeat ss
        where ss.screening.id = :screeningId
          and ss.status <> 'FREE'
    """)
    List<ScreeningSeat> findTakenSeats(Long screeningId);
}

