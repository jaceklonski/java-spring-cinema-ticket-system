package com.example.Cinema3D.controller;

import com.example.Cinema3D.service.SeatReservationService;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeatReservationController.class)
@Import(SeatReservationControllerTest.TestSecurityConfig.class)
class SeatReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatReservationService seatReservationService;

    @Test
    void shouldReserveSeat() throws Exception {
        mockMvc.perform(post("/api/v1/seats/reserve")
                        .param("screeningId", "1")
                        .param("row", "3")
                        .param("number", "5")
                        .with(user("john"))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(seatReservationService)
                .reserveSeat(1L, 3, 5, "john");
    }

    // ===================== TEST SECURITY =====================
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
