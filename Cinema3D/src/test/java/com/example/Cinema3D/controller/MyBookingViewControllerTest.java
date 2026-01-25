package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Booking;
import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyBookingViewController.class)
@Import(MyBookingViewControllerTest.TestSecurityConfig.class)
class MyBookingViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void shouldShowMyBookings() throws Exception {
        // ===== GIVEN =====
        Movie movie = new Movie();
        movie.setTitle("Test Movie");

        Screening screening = new Screening();
        screening.setMovie(movie);

        Booking booking = new Booking();
        booking.setScreening(screening);

        when(bookingService.getMyBookings())
                .thenReturn(List.of(booking));

        // ===== WHEN / THEN =====
        mockMvc.perform(get("/my-bookings"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-bookings"))
                .andExpect(model().attributeExists("bookings"));
    }

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );
            return http.build();
        }
    }
}
