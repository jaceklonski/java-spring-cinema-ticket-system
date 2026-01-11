package com.example.Cinema3D.service;

import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.mapper.MovieMapper;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie createMovie(MovieRequest request) {
    Movie movie = MovieMapper.toEntity(request);
    return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
