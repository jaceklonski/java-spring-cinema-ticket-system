package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.entity.Screening;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.MovieRepository;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieViewController.class)
@Import(MovieViewControllerTest.TestSecurityConfig.class)
class MovieViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private ScreeningRepository screeningRepository;

    @Test
    void shouldRenderMoviesList() throws Exception {
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Movie 1");

        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");

        Mockito.when(movieRepository.findAll())
                .thenReturn(List.of(movie1, movie2));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attribute("movies", hasSize(2)))
                .andExpect(model().attribute(
                        "movies",
                        hasItem(hasProperty("title", is("Movie 1")))
                ));
    }

    @Test
    void shouldRenderMovieDetails() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");

        Screening screening1 = new Screening();
        screening1.setMovie(movie);

        Screening screening2 = new Screening();
        screening2.setMovie(movie);

        Mockito.when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        Mockito.when(screeningRepository.findAll())
                .thenReturn(List.of(screening1, screening2));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movie-details"))
                .andExpect(model().attribute("movie", movie))
                .andExpect(model().attribute("screenings", hasSize(2)));
    }

    @Test
    void shouldThrowNotFoundWhenMovieDoesNotExist() throws Exception {
        Mockito.when(movieRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/movies/99"))
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
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );
            return http.build();
        }
    }
}
