package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.entity.ScreeningSeat;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CinemaViewController {

    private final ScreeningRepository screeningRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    @GetMapping("/cinema")
    public String cinema(Model model) {

        List<Screening> screenings = screeningRepository.findAll();
        model.addAttribute("screenings", screenings);

        return "cinema";
    }

    @GetMapping("/cinema/screening/{screeningId}")
    public String screeningSeats(
            @PathVariable Long screeningId,
            Model model,
            HttpSession session
    ) {
        session.setAttribute("SCREENING_ID", screeningId);

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException("Screening not found"));

        List<ScreeningSeat> seats =
                screeningSeatRepository.findByScreeningId(screeningId);

        Map<Integer, List<ScreeningSeat>> seatsByRow = seats.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getSeat().getRow(),
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(ss -> ss.getSeat().getNumber()))
                                        .toList()
                        )
                ));


        model.addAttribute("screening", screening);
        model.addAttribute("seatsByRow", seatsByRow);

        return "seats";
    }
}
