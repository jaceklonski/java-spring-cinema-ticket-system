package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.CinemaRoomCreateRequest;
import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.repository.CinemaRoomRepository;
import com.example.Cinema3D.repository.MovieRepository;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.service.CinemaRoomService;
import com.example.Cinema3D.service.MovieService;
import com.example.Cinema3D.service.ScreeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminViewController {

    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    private final CinemaRoomService cinemaRoomService;

    @GetMapping("/screenings")
    @Transactional(readOnly = true)
    public String screenings(Model model) {
        model.addAttribute("screenings", screeningRepository.findAll());
        return "admin/screenings";
    }

    @GetMapping("/screenings/new")
    @Transactional(readOnly = true)
    public String newScreeningForm(Model model) {
        model.addAttribute("screening", new ScreeningRequest());
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("rooms", cinemaRoomRepository.findAll());
        return "admin/screening-form";
    }

    @PostMapping("/screenings")
    public String createScreening(
            @Valid @ModelAttribute("screening") ScreeningRequest screening,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieRepository.findAll());
            model.addAttribute("rooms", cinemaRoomRepository.findAll());
            return "admin/screening-form";
        }

        screeningService.create(screening);
        return "redirect:/admin/screenings";
    }


    @GetMapping("/rooms")
    @Transactional(readOnly = true)
    public String rooms(Model model) {
        model.addAttribute("rooms", cinemaRoomRepository.findAll());
        return "admin/rooms";
    }

    @GetMapping("/rooms/new")
    public String newRoomForm(Model model) {
        model.addAttribute("room", new CinemaRoomCreateRequest());
        return "admin/room-form";
    }

    @PostMapping("/rooms")
    public String createRoom(
            @Valid @ModelAttribute("room") CinemaRoomCreateRequest room,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "admin/room-form";
        }

        cinemaRoomService.createRoom(room);
        return "redirect:/admin/rooms";
    }
}
