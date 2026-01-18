package com.example.Cinema3D.controller;

import com.example.Cinema3D.dto.screening.ScreeningRequest;
import com.example.Cinema3D.dto.screening.ScreeningResponse;
import com.example.Cinema3D.mapper.ScreeningMapper;
import com.example.Cinema3D.service.ScreeningService;
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
        name = "Screenings",
        description = "Operations related to movie screenings"
)
@RestController
@RequestMapping("/api/v1/screenings")
@RequiredArgsConstructor
public class ScreeningController {

    private final ScreeningService screeningService;

    @Operation(summary = "Get paginated list of screenings")
    @GetMapping
    public ResponseEntity<Page<ScreeningResponse>> getAll(
            @ParameterObject
            @PageableDefault(size = 10, sort = "startTime") Pageable pageable
    ) {
        return ResponseEntity.ok(
                screeningService.getAll(pageable)
                        .map(ScreeningMapper::toResponse)
        );
    }

    @Operation(summary = "Get screening by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ScreeningResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ScreeningMapper.toResponse(
                        screeningService.getById(id)
                )
        );
    }

    @Operation(summary = "Create new screening")
    @PostMapping
    public ResponseEntity<ScreeningResponse> create(
            @RequestBody @Valid ScreeningRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ScreeningMapper.toResponse(
                                screeningService.create(request)
                        )
                );
    }

    @Operation(summary = "Delete screening")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        screeningService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
