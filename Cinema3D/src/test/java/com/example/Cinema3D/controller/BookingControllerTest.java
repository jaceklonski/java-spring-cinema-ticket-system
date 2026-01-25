package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.repository.BookingRepository;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@Import(BookingControllerTest.TestSecurityConfig.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private ScreeningSeatRepository screeningSeatRepository;

    @MockBean
    private UserRepository userRepository;

    // ===========================
    // TESTS
    // ===========================

    @Test
    @WithMockUser(username = "john")
    void confirmBooking_shouldRedirect_whenNoSeats() throws Exception {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(screeningSeatRepository.findByReservedByAndStatus(
                user, SeatStatus.RESERVED))
                .thenReturn(List.of());

        mockMvc.perform(post("/booking/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ticketTypes", "NORMAL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser(username = "john")
    void confirmBooking_shouldCreateBooking() throws Exception {
        User user = new User();
        user.setUsername("john");

        Screening screening = new Screening();

        ScreeningSeat seat = new ScreeningSeat();
        seat.setStatus(SeatStatus.RESERVED);
        seat.setScreening(screening);

        Booking booking = new Booking();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(screeningSeatRepository.findByReservedByAndStatus(
                user, SeatStatus.RESERVED))
                .thenReturn(List.of(seat));

        when(bookingRepository.save(any()))
                .thenReturn(booking);

        mockMvc.perform(post("/booking/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ticketTypes", "NORMAL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/booking/my-bookings"));

        verify(screeningSeatRepository).saveAll(any());
    }

    @Test
    @WithMockUser(username = "john")
    void myBookings_shouldReturnView() throws Exception {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByUserOrderByIdDesc(user))
                .thenReturn(List.of());

        mockMvc.perform(get("/booking/my-bookings"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-bookings"))
                .andExpect(model().attributeExists("bookings"));
    }

    // ===========================
    // TEST SECURITY CONFIG
    // ===========================

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );
            return http.build();
        }
    }
}
