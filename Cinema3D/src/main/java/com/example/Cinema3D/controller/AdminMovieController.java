package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String form(Model model) {
        model.addAttribute("movieRequest", new MovieRequest());
        return "admin/movie-form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("movieRequest") MovieRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "admin/movie-form";
        }

        movieService.create(request);
        return "redirect:/admin/movies";
    }
}
