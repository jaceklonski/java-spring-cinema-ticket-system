package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    @Transactional
    public Screening create(ScreeningRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        CinemaRoom room = cinemaRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Cinema room not found"));

        Screening screening = new Screening();
        screening.setStartTime(request.getStartTime());
        screening.setMovie(movie);
        screening.setRoom(room);

        Screening saved = screeningRepository.save(screening);

        room.getSeats().forEach(seat -> {
            ScreeningSeat ss = new ScreeningSeat();
            ss.setScreening(saved);
            ss.setSeat(seat);
            ss.setStatus(SeatStatus.FREE);
            screeningSeatRepository.save(ss);
        });

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Screening> getAll() {
        return screeningRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Screening> getByMovie(Long movieId) {
        return screeningRepository.findByMovieId(movieId);
    }
}

