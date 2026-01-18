package com.example.Cinema3D.controller;

import com.example.Cinema3D.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyBookingViewController {

    private final BookingService bookingService;

    @GetMapping("/my-bookings")
    public String myBookings(Model model) {
        model.addAttribute("bookings", bookingService.getMyBookings());
        return "my-bookings";
    }
}
