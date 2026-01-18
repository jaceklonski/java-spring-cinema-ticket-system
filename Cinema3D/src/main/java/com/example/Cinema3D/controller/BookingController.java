package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.repository.BookingRepository;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ScreeningSeatRepository screeningSeatRepository;
    private final UserRepository userRepository;

    // ===============================
    // FINALIZACJA REZERWACJI
    // ===============================
    @PostMapping("/confirm")
    @Transactional
    public String confirmBooking(
            @RequestParam List<TicketType> ticketTypes,
            Principal principal
    ) {

        com.example.Cinema3D.entity.User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        List<ScreeningSeat> seats =
                screeningSeatRepository.findByReservedByAndStatus(
                        user,
                        SeatStatus.RESERVED
                );

        if (seats.isEmpty()) {
            return "redirect:/cart";
        }

        if (seats.size() != ticketTypes.size()) {
            throw new IllegalStateException("Nieprawidłowa liczba biletów");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(seats.get(0).getScreening());
        booking = bookingRepository.save(booking);

        for (int i = 0; i < seats.size(); i++) {
            ScreeningSeat ss = seats.get(i);
            ss.setStatus(SeatStatus.SOLD);
            ss.setBooking(booking);
            ss.setTicketType(ticketTypes.get(i));
        }

        screeningSeatRepository.saveAll(seats);

        return "redirect:/booking/my-bookings";
    }

    // ===============================
    // MOJE REZERWACJE
    // ===============================
    @GetMapping("/my-bookings")
    public String myBookings(Model model, Principal principal) {

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        model.addAttribute(
                "bookings",
                bookingRepository.findByUserOrderByIdDesc(user)
        );

        return "my-bookings";
    }
}
