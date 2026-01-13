package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.CinemaRoom;
import com.example.Cinema3D.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByRoomId(Long roomId);

    Optional<Seat> findByRoomAndRowAndNumber(
            CinemaRoom room,
            int row,
            int number
    );
}
