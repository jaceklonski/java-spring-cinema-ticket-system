package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.entity.*;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    @Mock
    ScreeningRepository screeningRepository;
    @Mock
    MovieRepository movieRepository;
    @Mock
    CinemaRoomRepository cinemaRoomRepository;
    @Mock
    ScreeningSeatRepository screeningSeatRepository;

    @InjectMocks
    ScreeningService screeningService;

    // ---------------- getAll ----------------

    @Test
    void getAll_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Screening screening = new Screening();

        when(screeningRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(screening)));

        Page<Screening> result = screeningService.getAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    // ---------------- getById ----------------

    @Test
    void getById_returnsScreening() {
        Screening screening = new Screening();
        screening.setId(1L);

        when(screeningRepository.findById(1L))
                .thenReturn(Optional.of(screening));

        Screening result = screeningService.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(screeningRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> screeningService.getById(1L)
        );
    }

    // ---------------- create ----------------

    @Test
    void create_createsScreeningAndSeats() {
        // movie
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setDurationMinutes(120);

        // room + seats
        Seat seat1 = new Seat();
        Seat seat2 = new Seat();
        CinemaRoom room = new CinemaRoom();
        room.setId(2L);
        room.setSeats(Set.of(seat1, seat2));


        ScreeningRequest request = new ScreeningRequest();
        request.setMovieId(1L);
        request.setRoomId(2L);
        request.setStartTime(LocalDateTime.of(2024, 1, 1, 18, 0));

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));
        when(cinemaRoomRepository.findById(2L))
                .thenReturn(Optional.of(room));
        when(screeningRepository.findOverlappingScreenings(
                eq(2L),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(List.of());

        when(screeningRepository.save(any(Screening.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Screening result = screeningService.create(request);

        assertNotNull(result);
        assertEquals(movie, result.getMovie());
        assertEquals(room, result.getRoom());

        verify(screeningSeatRepository, times(2))
                .save(any(ScreeningSeat.class));
    }

    @Test
    void create_throwsWhenMovieNotFound() {
        ScreeningRequest request = new ScreeningRequest();
        request.setMovieId(1L);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> screeningService.create(request)
        );
    }

    @Test
    void create_throwsWhenRoomNotFound() {
        Movie movie = new Movie();
        movie.setDurationMinutes(100);

        ScreeningRequest request = new ScreeningRequest();
        request.setMovieId(1L);
        request.setRoomId(2L);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));
        when(cinemaRoomRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> screeningService.create(request)
        );
    }

    @Test
    void create_throwsWhenOverlappingScreeningExists() {
        Movie movie = new Movie();
        movie.setDurationMinutes(90);

        CinemaRoom room = new CinemaRoom();
        room.setId(2L);

        ScreeningRequest request = new ScreeningRequest();
        request.setMovieId(1L);
        request.setRoomId(2L);
        request.setStartTime(LocalDateTime.now());

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));
        when(cinemaRoomRepository.findById(2L))
                .thenReturn(Optional.of(room));
        when(screeningRepository.findOverlappingScreenings(
                anyLong(),
                any(),
                any())
        ).thenReturn(List.of(new Screening()));

        assertThrows(
                IllegalStateException.class,
                () -> screeningService.create(request)
        );
    }

    // ---------------- delete ----------------

    @Test
    void delete_deletesWhenExists() {
        when(screeningRepository.existsById(1L))
                .thenReturn(true);

        screeningService.delete(1L);

        verify(screeningRepository).deleteById(1L);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(screeningRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                NotFoundException.class,
                () -> screeningService.delete(1L)
        );
    }
}
