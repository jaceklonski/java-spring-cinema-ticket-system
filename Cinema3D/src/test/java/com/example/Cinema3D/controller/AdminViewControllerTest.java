package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.CinemaRoomCreateRequest;
import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.repository.CinemaRoomRepository;
import com.example.Cinema3D.repository.MovieRepository;
import com.example.Cinema3D.repository.ScreeningRepository;
import com.example.Cinema3D.service.CinemaRoomService;
import com.example.Cinema3D.service.MovieService;
import com.example.Cinema3D.service.ScreeningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminViewController.class)
@WithMockUser(roles = "ADMIN")
class AdminViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private ScreeningService screeningService;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private ScreeningRepository screeningRepository;

    @MockBean
    private CinemaRoomRepository cinemaRoomRepository;

    @MockBean
    private CinemaRoomService cinemaRoomService;

    // ---------- SCREENINGS ----------

    @Test
    void shouldShowScreeningsList() throws Exception {
        when(screeningRepository.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/admin/screenings"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/screenings"))
                .andExpect(model().attributeExists("screenings"));
    }

    @Test
    void shouldShowNewScreeningForm() throws Exception {
        when(movieRepository.findAll()).thenReturn(java.util.List.of());
        when(cinemaRoomRepository.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/admin/screenings/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/screening-form"))
                .andExpect(model().attributeExists("screening"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("rooms"));
    }

    @Test
    void shouldCreateScreeningWhenValid() throws Exception {
        mockMvc.perform(post("/admin/screenings")
                        .with(csrf())
                        .param("startTime", "2030-01-01T20:00")
                        .param("movieId", "1")
                        .param("roomId", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/screenings"));

        verify(screeningService).create(any(ScreeningRequest.class));
    }

    @Test
    void shouldReturnFormWhenScreeningInvalid() throws Exception {
        when(movieRepository.findAll()).thenReturn(java.util.List.of());
        when(cinemaRoomRepository.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(post("/admin/screenings")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/screening-form"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("rooms"));
    }

    // ---------- ROOMS ----------

    @Test
    void shouldShowRoomsList() throws Exception {
        when(cinemaRoomRepository.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/admin/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/rooms"))
                .andExpect(model().attributeExists("rooms"));
    }

    @Test
    void shouldShowNewRoomForm() throws Exception {
        mockMvc.perform(get("/admin/rooms/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/room-form"))
                .andExpect(model().attributeExists("room"));
    }

    @Test
    void shouldCreateRoomWhenValid() throws Exception {
        mockMvc.perform(post("/admin/rooms")
                        .with(csrf())
                        .param("name", "Room A")
                        .param("rows", "5")
                        .param("seatsPerRow", "10")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/rooms"));

        verify(cinemaRoomService)
                .createRoom(any(CinemaRoomCreateRequest.class));
    }

    @Test
    void shouldReturnRoomFormWhenInvalid() throws Exception {
        mockMvc.perform(post("/admin/rooms")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/room-form"));
    }
}
