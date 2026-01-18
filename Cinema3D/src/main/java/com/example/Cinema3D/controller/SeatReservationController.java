package com.example.Cinema3D.controller;

import com.example.Cinema3D.service.SeatReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatReservationController {

    private final SeatReservationService seatReservationService;

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveSeat(
            @RequestParam Long screeningId,
            @RequestParam int row,
            @RequestParam int number,
            Principal principal
    ) {
        seatReservationService.reserveSeat(
                screeningId,
                row,
                number,
                principal.getName()
        );
        return ResponseEntity.ok().build();
    }
}
