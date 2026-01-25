package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieService movieService;

    @TempDir
    Path tempDir;

    @Test
    void getAll_returnsPagedMovieResponses() {
        Movie movie = new Movie();
        movie.setTitle("Matrix");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Movie> page = new PageImpl<>(List.of(movie));

        when(movieRepository.findAll(pageable)).thenReturn(page);

        Page<MovieResponse> result = movieService.getAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Matrix", result.getContent().get(0).getTitle());
    }

    @Test
    void getByIdDto_returnsMovie() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        MovieResponse response = movieService.getByIdDto(1L);

        assertEquals("Inception", response.getTitle());
    }

    @Test
    void getByIdDto_throwsWhenNotFound() {
        when(movieRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> movieService.getByIdDto(1L)
        );
    }

    @Test
    void create_createsMovieWithoutCover() {
        MovieRequest request = new MovieRequest();
        request.setTitle("Avatar");
        request.setDurationMinutes(160);

        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MovieResponse response = movieService.create(request);

        assertEquals("Avatar", response.getTitle());
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void create_createsMovieWithCover() {
        ReflectionTestUtils.setField(
                movieService,
                "uploadDir",
                tempDir.toString()
        );

        MockMultipartFile file = new MockMultipartFile(
                "cover",
                "poster.jpg",
                "image/jpeg",
                "image-content".getBytes()
        );

        MovieRequest request = new MovieRequest();
        request.setTitle("Interstellar");
        request.setDurationMinutes(169);
        request.setCover(file);

        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MovieResponse response = movieService.create(request);

        assertNotNull(response.getCoverUrl());
        assertTrue(response.getCoverUrl().startsWith("/uploads/movies/"));
    }

    @Test
    void update_updatesMovieWithoutCover() {
        Movie movie = new Movie();
        movie.setId(1L);

        MovieRequest request = new MovieRequest();
        request.setTitle("Updated");
        request.setDurationMinutes(120);
        request.setDescription("desc");

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class)))
                .thenReturn(movie);

        MovieResponse response =
                movieService.update(1L, request);

        assertEquals("Updated", response.getTitle());
    }

    @Test
    void update_throwsWhenMovieNotFound() {
        when(movieRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> movieService.update(1L, new MovieRequest())
        );
    }

    @Test
    void delete_deletesWhenExists() {
        when(movieRepository.existsById(1L)).thenReturn(true);

        movieService.delete(1L);

        verify(movieRepository).deleteById(1L);
    }

    @Test
    void delete_throwsWhenNotFound() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        assertThrows(
                NotFoundException.class,
                () -> movieService.delete(1L)
        );
    }

    @Test
    void saveCover_throwsRuntimeExceptionOnIOException() throws IOException {
        ReflectionTestUtils.setField(
                movieService,
                "uploadDir",
                tempDir.toString()
        );

        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(IOException.class); // 🔴 only needed stub

        MovieRequest request = new MovieRequest();
        request.setTitle("Error");
        request.setDurationMinutes(100);
        request.setCover(file);

        assertThrows(
                RuntimeException.class,
                () -> movieService.create(request)
        );
    }
}
