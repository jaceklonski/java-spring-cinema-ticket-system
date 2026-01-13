package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.entity.CinemaRoom;
import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.CinemaRoomRepository;
import com.example.Cinema3D.repository.MovieRepository;
import com.example.Cinema3D.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaRoomRepository cinemaRoomRepository;

    public Screening create(ScreeningRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        CinemaRoom room = cinemaRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NotFoundException("Cinema room not found"));

        Screening screening = new Screening();
        screening.setStartTime(request.getStartTime());
        screening.setMovie(movie);
        screening.setRoom(room);

        return screeningRepository.save(screening);
    }

    public List<Screening> getAll() {
        return screeningRepository.findAll();
    }

    public List<Screening> getByMovie(Long movieId) {
        return screeningRepository.findByMovieId(movieId);
    }
}
