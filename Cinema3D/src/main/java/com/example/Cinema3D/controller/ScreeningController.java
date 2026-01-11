package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.dto.screening.ScreeningResponse;
import com.example.Cinema3D.mapper.ScreeningMapper;
import com.example.Cinema3D.service.ScreeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screenings")
@RequiredArgsConstructor
public class ScreeningController {

    private final ScreeningService screeningService;

    @PostMapping
    public ScreeningResponse create(@RequestBody @Valid ScreeningRequest request) {
        return ScreeningMapper.toResponse(
                screeningService.create(request)
        );
    }

    @GetMapping
    public List<ScreeningResponse> getAll() {
        return screeningService.getAll()
                .stream()
                .map(ScreeningMapper::toResponse)
                .toList();
    }

    @GetMapping("/movie/{movieId}")
    public List<ScreeningResponse> getByMovie(@PathVariable Long movieId) {
        return screeningService.getByMovie(movieId)
                .stream()
                .map(ScreeningMapper::toResponse)
                .toList();
    }
}
