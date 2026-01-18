package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.ScreeningSeat;
import com.example.Cinema3D.entity.SeatStatus;
import com.example.Cinema3D.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningSeatRepository extends JpaRepository<ScreeningSeat, Long> {


    Optional<ScreeningSeat> findByScreeningIdAndSeatId(
            Long screeningId,
            Long seatId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select ss
        from ScreeningSeat ss
        where ss.screening.id = :screeningId
          and ss.seat.id = :seatId
    """)
    Optional<ScreeningSeat> findForUpdate(
            @Param("screeningId") Long screeningId,
            @Param("seatId") Long seatId
    );


    List<ScreeningSeat> findByScreeningId(Long screeningId);

    Optional<ScreeningSeat> findByScreeningIdAndSeatRowAndSeatNumber(
            Long screeningId,
            int row,
            int number
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select ss
        from ScreeningSeat ss
        where ss.screening.id = :screeningId
          and ss.seat.row = :row
          and ss.seat.number = :number
    """)
    Optional<ScreeningSeat> lockSeat(
            @Param("screeningId") Long screeningId,
            @Param("row") int row,
            @Param("number") int number
    );


    @Query("""
        select ss
        from ScreeningSeat ss
        where ss.status = :status
          and ss.reservedAt < :time
    """)
    List<ScreeningSeat> findExpiredReservations(
            @Param("status") SeatStatus status,
            @Param("time") LocalDateTime time
    );


    @Query("""
        select ss
        from ScreeningSeat ss
        where ss.status <> 'FREE'
          and ss.screening.id = :screeningId
    """)
    List<ScreeningSeat> findTakenSeats(@Param("screeningId") Long screeningId);
    List<ScreeningSeat> findByReservedByAndStatus(User user, SeatStatus status);

}
