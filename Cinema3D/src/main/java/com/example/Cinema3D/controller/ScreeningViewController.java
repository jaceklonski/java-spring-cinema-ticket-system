package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ScreeningViewController {

    private final ScreeningRepository screeningRepository;

    @GetMapping("/screenings")
    public String screenings(
            @RequestParam(required = false) LocalDate date,
            Model model
    ) {
        // domyślnie: dzisiaj
        if (date == null) {
            date = LocalDate.now();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Screening> screenings =
                screeningRepository.findByStartTimeBetween(start, end);

        // grupowanie: FILM -> SEANSE
        Map<Long, List<Screening>> screeningsByMovie =
                screenings.stream()
                        .collect(Collectors.groupingBy(
                                s -> s.getMovie().getId()
                        ));

        model.addAttribute("date", date);
        model.addAttribute("screeningsByMovie", screeningsByMovie);

        return "screenings";
    }
}
