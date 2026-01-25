package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
import com.example.Cinema3D.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@Import(MovieControllerTest.TestSecurityConfig.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    // ===================== GET =====================

    @Test
    void shouldReturnPagedMovies() throws Exception {
        MovieResponse movie = new MovieResponse(
                1L, "Matrix", 136, "Sci-fi", "cover.jpg",
                "SCI-FI", "16+", "Wachowski",
                List.of("Keanu Reeves"), null, null
        );

        Page<MovieResponse> page =
                new PageImpl<>(List.of(movie), PageRequest.of(0, 10), 1);

        Mockito.when(movieService.getAll(Mockito.any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/movies")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Matrix"));
    }

    @Test
    void shouldReturnMovieById() throws Exception {
        MovieResponse movie = new MovieResponse(
                1L, "Matrix", 136, "Sci-fi", "cover.jpg",
                "SCI-FI", "16+", "Wachowski",
                List.of("Keanu Reeves"), null, null
        );

        Mockito.when(movieService.getByIdDto(1L))
                .thenReturn(movie);

        mockMvc.perform(get("/api/v1/movies/1")
                        .with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Matrix"));
    }

    // ===================== POST =====================

    @Test
    void shouldCreateMovie() throws Exception {
        MovieResponse movie = new MovieResponse(
                1L, "Matrix", 136, null, null,
                "SCI-FI", "16+", "Wachowski",
                null, null, null
        );

        Mockito.when(movieService.create(Mockito.any(MovieRequest.class)))
                .thenReturn(movie);

        MockMultipartFile cover = new MockMultipartFile(
                "cover", "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image".getBytes()
        );

        mockMvc.perform(
                        multipart("/api/v1/movies")
                                .file(cover)
                                .param("title", "Matrix")
                                .param("durationMinutes", "136")
                                .param("genre", "SCI-FI")
                                .param("ageRating", "16+")
                                .param("director", "Wachowski")
                                .with(csrf())
                                .with(user("admin"))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Matrix"));
    }

    // ===================== PUT =====================

    @Test
    void shouldUpdateMovie() throws Exception {
        MovieResponse movie = new MovieResponse(
                1L, "Matrix Reloaded", 138, null, null,
                "SCI-FI", "16+", "Wachowski",
                null, null, null
        );

        Mockito.when(movieService.update(Mockito.eq(1L), Mockito.any(MovieRequest.class)))
                .thenReturn(movie);

        MockMultipartFile cover = new MockMultipartFile(
                "cover", "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image".getBytes()
        );

        mockMvc.perform(
                        multipart("/api/v1/movies/1")
                                .file(cover)
                                .param("title", "Matrix Reloaded")
                                .param("durationMinutes", "138")
                                .param("genre", "SCI-FI")
                                .param("ageRating", "16+")
                                .param("director", "Wachowski")
                                .with(csrf())
                                .with(user("admin"))
                                .with(req -> {
                                    req.setMethod("PUT");
                                    return req;
                                })
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Matrix Reloaded"));
    }

    // ===================== DELETE =====================

    @Test
    void shouldDeleteMovie() throws Exception {
        Mockito.doNothing().when(movieService).delete(1L);

        mockMvc.perform(delete("/api/v1/movies/1")
                        .with(csrf())
                        .with(user("admin")))
                .andExpect(status().isNoContent());
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
