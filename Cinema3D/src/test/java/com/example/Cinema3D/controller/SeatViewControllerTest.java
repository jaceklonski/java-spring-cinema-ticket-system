package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.entity.ScreeningSeat;
import com.example.Cinema3D.repository.ScreeningRepository;
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
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatViewController.class)
@Import(SeatViewControllerTest.TestSecurityConfig.class)
class SeatViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningRepository screeningRepository;

    @MockBean
    private ScreeningSeatRepository screeningSeatRepository;

    @Test
    void shouldReturnSeatsView() throws Exception {
        // ===== GIVEN =====
        Movie movie = new Movie();
        movie.setTitle("Test Movie");

        Screening screening = new Screening();
        screening.setId(1L);
        screening.setMovie(movie);

        Mockito.when(screeningRepository.findById(1L))
                .thenReturn(Optional.of(screening));

        Mockito.when(screeningSeatRepository.findByScreeningId(1L))
                .thenReturn(List.of(
                        new ScreeningSeat(),
                        new ScreeningSeat()
                ));

        // ===== WHEN / THEN =====
        mockMvc.perform(get("/screenings/1/seats")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("seats"))
                .andExpect(model().attributeExists("screening"))
                .andExpect(model().attributeExists("seats"));
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
