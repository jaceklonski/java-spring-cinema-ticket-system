package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.seat.ScreeningSeatDto;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/screenings")
@RequiredArgsConstructor
public class ScreeningSeatController {

    private final ScreeningSeatRepository screeningSeatRepository;

    @GetMapping("/{screeningId}/seats")
    public List<ScreeningSeatDto> getSeats(@PathVariable Long screeningId) {
        return screeningSeatRepository.findByScreeningId(screeningId)
                .stream()
                .map(ss -> new ScreeningSeatDto(
                        ss.getSeat().getRow(),
                        ss.getSeat().getNumber(),
                        ss.getStatus()
                ))
                .toList();
    }
}

