package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.entity.CinemaRoom;
import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.service.ScreeningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScreeningController.class)
@Import(ScreeningControllerTest.TestSecurityConfig.class)
class ScreeningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScreeningService screeningService;

    // =========================
    // GET /api/v1/screenings
    // =========================
    @Test
    void shouldReturnPagedScreenings() throws Exception {
        Screening screening = buildScreening(1L);
        Page<Screening> page = new PageImpl<>(List.of(screening));

        when(screeningService.getAll(any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/screenings")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    void shouldReturnScreeningById() throws Exception {
        Screening screening = buildScreening(5L);

        when(screeningService.getById(5L))
                .thenReturn(screening);

        mockMvc.perform(get("/api/v1/screenings/5")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.movieTitle").value("Test Movie"))
                .andExpect(jsonPath("$.roomName").value("Room 1"));
    }

    @Test
    void shouldCreateScreening() throws Exception {
        ScreeningRequest request = new ScreeningRequest();
        request.setStartTime(LocalDateTime.now());
        request.setMovieId(10L);
        request.setRoomId(20L);

        Screening screening = buildScreening(99L);

        when(screeningService.create(any()))
                .thenReturn(screening);

        mockMvc.perform(post("/api/v1/screenings")
                        .with(user("admin"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99L));
    }

    @Test
    void shouldDeleteScreening() throws Exception {
        mockMvc.perform(delete("/api/v1/screenings/7")
                        .with(user("admin"))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(screeningService).delete(7L);
    }

    private Screening buildScreening(Long id) {
        Movie movie = new Movie();
        movie.setId(10L);
        movie.setTitle("Test Movie");

        CinemaRoom room = new CinemaRoom();
        room.setId(20L);
        room.setName("Room 1");

        Screening screening = new Screening();
        screening.setId(id);
        screening.setStartTime(LocalDateTime.now());
        screening.setMovie(movie);
        screening.setRoom(room);

        return screening;
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
