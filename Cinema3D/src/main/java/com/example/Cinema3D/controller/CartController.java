package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.SeatStatus;
import com.example.Cinema3D.entity.TicketType;
import com.example.Cinema3D.entity.User;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final ScreeningSeatRepository screeningSeatRepository;
    private final UserRepository userRepository;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        model.addAttribute(
                "items",
                screeningSeatRepository.findByReservedByAndStatus(
                        user,
                        SeatStatus.RESERVED
                )
        );

        model.addAttribute("ticketTypes", TicketType.values());

        return "cart";
    }
}
