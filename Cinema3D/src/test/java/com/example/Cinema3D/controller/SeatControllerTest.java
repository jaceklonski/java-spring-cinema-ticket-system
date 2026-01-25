package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
@Import(SeatControllerTest.TestSecurityConfig.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningRepository screeningRepository;

    @MockBean
    private SeatRepository seatRepository;

    @Test
    void shouldReturnSeatsForScreening() throws Exception {
        CinemaRoom room = new CinemaRoom();
        room.setId(10L);

        Screening screening = new Screening();
        screening.setRoom(room);

        Seat seat = new Seat();
        seat.setRow(2);
        seat.setNumber(7);

        Mockito.when(screeningRepository.findById(1L))
                .thenReturn(Optional.of(screening));

        Mockito.when(seatRepository.findByRoomId(10L))
                .thenReturn(List.of(seat));

        mockMvc.perform(get("/api/seats/screening/1")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].row").value(2))
                .andExpect(jsonPath("$[0].number").value(7));
    }

    @Test
    void shouldReturn404WhenScreeningNotFound() throws Exception {
        Mockito.when(screeningRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/seats/screening/99")
                        .with(user("admin")))
                .andExpect(status().isNotFound());
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
