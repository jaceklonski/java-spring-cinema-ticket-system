package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    Page<Screening> findAll(Pageable pageable);

    @Query("""
        select s
        from Screening s
        where s.room.id = :roomId
          and s.startTime < :endTime
          and (s.startTime + s.movie.durationMinutes * 1 minute) > :startTime
    """)
    List<Screening> findOverlappingScreenings(
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );


    List<Screening> findByStartTimeBetween(
            LocalDateTime start,
            LocalDateTime end
    );

}
