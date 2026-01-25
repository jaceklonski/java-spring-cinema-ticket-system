package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.MovieRepository;
import com.example.Cinema3D.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieViewController {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;

    @GetMapping
    public String movies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "movies";
    }

    @GetMapping("/{id}")
    public String movieDetails(
            @PathVariable Long id,
            Model model
    ) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        model.addAttribute("movie", movie);
        model.addAttribute(
                "screenings",
                screeningRepository.findAll()
                        .stream()
                        .filter(s -> s.getMovie().getId().equals(id))
                        .toList()
        );

        return "movie-details";
    }
}
