package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.repository.ScreeningRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScreeningViewController.class)
@Import(ScreeningViewControllerTest.TestSecurityConfig.class)
class ScreeningViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningRepository screeningRepository;

    @Test
    void shouldRenderScreeningsView() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setStartTime(LocalDateTime.now());

        Mockito.when(
                screeningRepository.findByStartTimeBetween(
                        Mockito.any(), Mockito.any()
                )
        ).thenReturn(List.of(screening));

        mockMvc.perform(get("/screenings")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(view().name("screenings"))
                .andExpect(model().attributeExists("screeningsByMovie"))
                .andExpect(model().attributeExists("date"));
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
