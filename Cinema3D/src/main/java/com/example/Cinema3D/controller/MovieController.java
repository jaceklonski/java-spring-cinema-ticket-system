package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.mapper.MovieMapper;
import com.example.Cinema3D.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Movies",
        description = "Operations related to movie catalog"
)
@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "Get paginated list of movies")
    @GetMapping
    public ResponseEntity<Page<MovieResponse>> getAll(
            @ParameterObject
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        return ResponseEntity.ok(
                movieService.getAll(pageable)
                        .map(m -> new MovieResponse(
                                m.getId(),
                                m.getTitle(),
                                m.getDurationMinutes()
                        ))
        );
    }

    @Operation(summary = "Get movie by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getById(@PathVariable Long id) {
        Movie movie = movieService.getById(id);

        return ResponseEntity.ok(
                new MovieResponse(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getDurationMinutes()
                )
        );
    }

    @Operation(summary = "Create new movie (without cover)")
    @PostMapping
    public ResponseEntity<MovieResponse> create(
            @RequestBody @Valid MovieRequest request
    ) {
        // ignorujemy cover w REST
        request.setCover(null);

        Movie movie = movieService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new MovieResponse(
                                movie.getId(),
                                movie.getTitle(),
                                movie.getDurationMinutes()
                        )
                );
    }

    @Operation(summary = "Update existing movie")
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid MovieRequest request
    ) {
        request.setCover(null);

        Movie movie = movieService.update(id, request);

        return ResponseEntity.ok(
                new MovieResponse(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getDurationMinutes()
                )
        );
    }

    @Operation(summary = "Delete movie")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
