package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.CinemaRoomCreateRequest;
import com.example.Cinema3D.entity.CinemaRoom;
import com.example.Cinema3D.entity.Seat;
import com.example.Cinema3D.repository.CinemaRoomRepository;
import com.example.Cinema3D.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CinemaRoomService {

    private final CinemaRoomRepository cinemaRoomRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void createRoom(CinemaRoomCreateRequest request) {

        CinemaRoom room = new CinemaRoom();
        room.setName(request.getName());

        cinemaRoomRepository.save(room);

        for (int row = 1; row <= request.getRows(); row++) {
            for (int number = 1; number <= request.getSeatsPerRow(); number++) {

                Seat seat = new Seat();
                seat.setRow(row);
                seat.setNumber(number);
                seat.setRoom(room);

                seatRepository.save(seat);
            }
        }
    }
}
