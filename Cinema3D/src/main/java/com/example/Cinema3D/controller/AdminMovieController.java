package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
import com.example.Cinema3D.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/movies")
public class AdminMovieController {

    private final MovieService movieService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("movies",
                movieService.getAll(PageRequest.of(0, 20)));
        return "admin/movies";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("movieRequest", new MovieRequest());
        return "admin/movie-form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("movieRequest") MovieRequest request,
            BindingResult result,
            @RequestParam(value = "actorsText", required = false) String actorsText,
            @RequestParam(value = "galleryText", required = false) String galleryText
    ) {
        if (result.hasErrors()) {
            return "admin/movie-form";
        }

        request.setActors(parseLines(actorsText));
        request.setGalleryImages(parseLines(galleryText));

        movieService.create(request);
        return "redirect:/admin/movies";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        MovieResponse movie = movieService.getByIdDto(id);

        MovieRequest request = new MovieRequest();
        request.setTitle(movie.getTitle());
        request.setDurationMinutes(movie.getDurationMinutes());
        request.setDescription(movie.getDescription());
        request.setGenre(movie.getGenre());
        request.setAgeRating(movie.getAgeRating());
        request.setDirector(movie.getDirector());
        request.setActors(movie.getActors());
        request.setTrailerUrl(movie.getTrailerUrl());
        request.setGalleryImages(movie.getGalleryImages());

        model.addAttribute("movieId", id);
        model.addAttribute("movieRequest", request);

        // movie (MovieResponse) zostawiam do wyświetlenia np. aktualnej okładki
        model.addAttribute("movie", movie);

        return "admin/movie-edit";
    }

    @GetMapping("/movie-edit")
    public String editFormByParam(@RequestParam("id") Long id, Model model) {
        return editForm(id, model);
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("movieRequest") MovieRequest request,
            BindingResult result,
            @RequestParam(value = "actorsText", required = false) String actorsText,
            @RequestParam(value = "galleryText", required = false) String galleryText
    ) {
        if (result.hasErrors()) {
            return "admin/movie-edit";
        }

        request.setActors(parseLines(actorsText));
        request.setGalleryImages(parseLines(galleryText));

        movieService.update(id, request);
        return "redirect:/admin/movies";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/admin/movies";
    }

    private List<String> parseLines(String text) {
        if (text == null) return Collections.emptyList();
        return Arrays.stream(text.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
