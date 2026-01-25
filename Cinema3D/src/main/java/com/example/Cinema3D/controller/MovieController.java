package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Movies", description = "Operations related to movie catalog")
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
        return ResponseEntity.ok(movieService.getAll(pageable));
    }

    @Operation(summary = "Get movie by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getByIdDto(id));
    }

    // Zmieniono na @ModelAttribute i consumes MULTIPART_FORM_DATA_VALUE
    // aby umożliwić przesyłanie plików (okładek)
    @Operation(summary = "Create new movie with optional cover")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MovieResponse> create(
            @Valid @ModelAttribute MovieRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movieService.create(request));
    }

    @Operation(summary = "Update existing movie")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MovieResponse> update(
            @PathVariable Long id,
            @Valid @ModelAttribute MovieRequest request
    ) {
        return ResponseEntity.ok(movieService.update(id, request));
    }

    @Operation(summary = "Delete movie")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}