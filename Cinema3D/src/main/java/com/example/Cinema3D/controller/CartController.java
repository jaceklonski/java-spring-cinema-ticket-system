package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.SeatStatus;
import com.example.Cinema3D.entity.TicketType;
import com.example.Cinema3D.entity.User;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import com.example.Cinema3D.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final ScreeningSeatRepository screeningSeatRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

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

    @PostMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam Long screeningId,
            @RequestParam int row,
            @RequestParam int seat,
            HttpSession session
    ) {
        cartService.removeSeat(session, screeningId, row, seat);
        return "redirect:/cart";
    }
}
