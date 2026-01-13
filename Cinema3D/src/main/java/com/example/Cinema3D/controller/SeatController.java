package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.seat.SeatDto;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;

    @GetMapping("/screening/{screeningId}")
    public List<SeatDto> getSeatsForScreening(@PathVariable Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException("Screening not found"));

        return seatRepository.findByRoomId(screening.getRoom().getId())
                .stream()
                .map(seat -> new SeatDto(seat.getRow(), seat.getNumber()))
                .toList();
    }
}
