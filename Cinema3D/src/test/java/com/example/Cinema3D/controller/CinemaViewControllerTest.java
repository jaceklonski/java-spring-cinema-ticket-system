package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CinemaViewController.class)
@Import(CinemaViewControllerTest.TestSecurityConfig.class)
class CinemaViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningRepository screeningRepository;

    @MockBean
    private ScreeningSeatRepository screeningSeatRepository;

    @Test
    void cinema_shouldReturnCinemaViewWithScreenings() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");

        Screening screening = new Screening();
        screening.setId(1L);
        screening.setMovie(movie);

        when(screeningRepository.findAll())
                .thenReturn(List.of(screening));

        mockMvc.perform(get("/cinema"))
                .andExpect(status().isOk())
                .andExpect(view().name("cinema"))
                .andExpect(model().attributeExists("screenings"))
                .andExpect(model().attribute("screenings", hasSize(1)));
    }

    @Test
    void screeningSeats_shouldReturnSeatsViewAndGroupSeatsByRow() throws Exception {
        Long screeningId = 1L;

        Movie movie = new Movie();
        movie.setTitle("Test Movie");

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setMovie(movie);

        Seat seat1 = new Seat();
        seat1.setRow(1);
        seat1.setNumber(2);

        Seat seat2 = new Seat();
        seat2.setRow(1);
        seat2.setNumber(1);

        ScreeningSeat ss1 = new ScreeningSeat();
        ss1.setSeat(seat1);

        ScreeningSeat ss2 = new ScreeningSeat();
        ss2.setSeat(seat2);

        when(screeningRepository.findById(screeningId))
                .thenReturn(Optional.of(screening));

        when(screeningSeatRepository.findByScreeningId(screeningId))
                .thenReturn(List.of(ss1, ss2));

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/cinema/screening/{id}", screeningId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("seats"))
                .andExpect(model().attributeExists("screening"))
                .andExpect(model().attributeExists("seatsByRow"))
                .andExpect(request().sessionAttribute("SCREENING_ID", screeningId));
    }

    @Test
    void screeningSeats_shouldThrowNotFound_whenScreeningDoesNotExist() throws Exception {
        when(screeningRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/cinema/screening/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertInstanceOf(
                                NotFoundException.class,
                                result.getResolvedException()
                        )
                );
    }

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
