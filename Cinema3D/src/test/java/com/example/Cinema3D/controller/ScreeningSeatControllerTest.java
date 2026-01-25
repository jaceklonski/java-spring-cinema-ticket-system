package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScreeningSeatController.class)
@Import(ScreeningSeatControllerTest.TestSecurityConfig.class)
class ScreeningSeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningSeatRepository screeningSeatRepository;

    @Test
    void shouldReturnSeatsForScreening() throws Exception {
        // ===== GIVEN =====
        Seat seat = new Seat();
        seat.setRow(1);
        seat.setNumber(5);

        ScreeningSeat ss = new ScreeningSeat();
        ss.setSeat(seat);
        ss.setStatus(SeatStatus.RESERVED);

        Mockito.when(screeningSeatRepository.findByScreeningId(1L))
                .thenReturn(List.of(ss));

        // ===== WHEN / THEN =====
        mockMvc.perform(get("/api/v1/screenings/1/seats")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].row").value(1))
                .andExpect(jsonPath("$[0].number").value(5))
                .andExpect(jsonPath("$[0].status").value("RESERVED"));
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
