package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.booking.BookingRequest;
import com.example.Cinema3D.dto.seat.SeatDto;
import com.example.Cinema3D.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingViewController.class)
@Import(BookingViewControllerTest.TestSecurityConfig.class)
class BookingViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void shouldCreateBookingFromSeatsString() throws Exception {
        // given
        Long screeningId = 10L;
        String seats = "1-2,1-3,2-5";

        ArgumentCaptor<BookingRequest> captor =
                ArgumentCaptor.forClass(BookingRequest.class);

        // when
        mockMvc.perform(post("/cinema/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("screeningId", screeningId.toString())
                        .param("seats", seats))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-bookings"));

        // then
        verify(bookingService).create(captor.capture());

        BookingRequest request = captor.getValue();
        assertThat(request.getScreeningId()).isEqualTo(screeningId);

        Set<SeatDto> seatDtos = request.getSeats();
        assertThat(seatDtos).hasSize(3);

        assertThat(seatDtos)
                .anyMatch(s -> s.getRow() == 1 && s.getNumber() == 2)
                .anyMatch(s -> s.getRow() == 1 && s.getNumber() == 3)
                .anyMatch(s -> s.getRow() == 2 && s.getNumber() == 5);
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
