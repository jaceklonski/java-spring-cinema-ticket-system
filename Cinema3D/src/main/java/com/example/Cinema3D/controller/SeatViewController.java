package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/screenings")
public class SeatViewController {

    private final ScreeningRepository screeningRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    @GetMapping("/{id}/seats")
    public String seats(@PathVariable Long id, Model model) {

        Screening screening = screeningRepository.findById(id)
                .orElseThrow();

        model.addAttribute("screening", screening);
        model.addAttribute(
                "seats",
                screeningSeatRepository.findByScreeningId(id)
        );

        return "seats";
    }
}
