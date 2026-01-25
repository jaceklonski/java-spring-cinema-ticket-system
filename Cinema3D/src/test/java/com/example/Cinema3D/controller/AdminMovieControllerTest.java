package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
import com.example.Cinema3D.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminMovieController.class)
@WithMockUser(roles = "ADMIN")
class AdminMovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    void shouldListMovies() throws Exception {
        when(movieService.getAll(PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/admin/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/movies"))
                .andExpect(model().attributeExists("movies"));
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/admin/movies/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/movie-form"))
                .andExpect(model().attributeExists("movieRequest"));
    }

    @Test
    void shouldCreateMovieWhenValid() throws Exception {
        mockMvc.perform(post("/admin/movies")
                        .with(csrf())
                        .param("title", "Inception")
                        .param("durationMinutes", "148")
                        .param("genre", "Sci-Fi")
                        .param("ageRating", "PG-13")
                        .param("director", "Nolan")
                        .param("actorsText", "Leonardo\nJoseph")
                        .param("galleryText", "img1\nimg2")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/movies"));

        verify(movieService).create(any(MovieRequest.class));
    }

    @Test
    void shouldReturnFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/admin/movies")
                        .with(csrf())
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/movie-form"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        MovieResponse response = new MovieResponse(
                1L,
                "Matrix",
                136,
                "Desc",
                "/cover.jpg",
                "Sci-Fi",
                "R",
                "Wachowski",
                List.of("Keanu"),
                "yt",
                List.of("img1")
        );

        when(movieService.getByIdDto(1L)).thenReturn(response);

        mockMvc.perform(get("/admin/movies/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/movie-edit"))
                .andExpect(model().attributeExists("movie"))
                .andExpect(model().attributeExists("movieRequest"))
                .andExpect(model().attribute("movieId", 1L));
    }

    @Test
    void shouldRedirectEditFormByParam() throws Exception {
        MovieResponse response = new MovieResponse(
                1L,
                "Matrix",
                136,
                null,
                null,
                "Sci-Fi",
                "R",
                "Wachowski",
                List.of(),
                null,
                List.of()
        );

        when(movieService.getByIdDto(1L)).thenReturn(response);

        mockMvc.perform(get("/admin/movies/movie-edit")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/movie-edit"));
    }

    @Test
    void shouldUpdateMovieWhenValid() throws Exception {
        mockMvc.perform(post("/admin/movies/1")
                        .with(csrf())
                        .param("title", "Updated")
                        .param("durationMinutes", "120")
                        .param("genre", "Drama")
                        .param("ageRating", "PG")
                        .param("director", "Someone")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/movies"));

        verify(movieService).update(eq(1L), any(MovieRequest.class));
    }

    @Test
    void shouldReturnEditFormWhenUpdateInvalid() throws Exception {
        mockMvc.perform(post("/admin/movies/1")
                        .with(csrf())
                        .param("title", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/movie-edit"));
    }

    @Test
    void shouldDeleteMovie() throws Exception {
        mockMvc.perform(post("/admin/movies/1/delete")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/movies"));

        verify(movieService).delete(1L);
    }
}
