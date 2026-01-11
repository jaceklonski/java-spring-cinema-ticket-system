package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
import com.example.Cinema3D.mapper.MovieMapper;
import com.example.Cinema3D.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies()
                .stream()
                .map(MovieMapper::toResponse)
                .toList();
    }

    @PostMapping
    public MovieResponse createMovie(@RequestBody @Valid MovieRequest request) {
        return MovieMapper.toResponse(
                movieService.createMovie(request)
        );
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
