package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    @Transactional(readOnly = true)
    public Page<Screening> getAll(Pageable pageable) {
        return screeningRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Screening getById(Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Screening not found"));
    }

    @Transactional
    public Screening create(ScreeningRequest request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        CinemaRoom room = cinemaRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Cinema room not found"));

        LocalDateTime start = request.getStartTime();
        LocalDateTime end = start.plusMinutes(movie.getDurationMinutes());

        // 🔒 WALIDACJA KOLIZJI CZASOWEJ
        List<Screening> overlapping = screeningRepository
                .findOverlappingScreenings(room.getId(), start, end);

        if (!overlapping.isEmpty()) {
            throw new IllegalStateException(
                    "Screening overlaps with another screening in the same room"
            );
        }

        Screening screening = new Screening();
        screening.setStartTime(start);
        screening.setMovie(movie);
        screening.setRoom(room);

        Screening saved = screeningRepository.save(screening);

        // przypisanie miejsc do seansu
        room.getSeats().forEach(seat -> {
            ScreeningSeat ss = new ScreeningSeat();
            ss.setScreening(saved);
            ss.setSeat(seat);
            ss.setStatus(SeatStatus.FREE);
            screeningSeatRepository.save(ss);
        });

        return saved;
    }

    @Transactional
    public void delete(Long id) {
        if (!screeningRepository.existsById(id)) {
            throw new NotFoundException("Screening not found");
        }
        screeningRepository.deleteById(id);
    }
}
