package com.example.Cinema3D.dto.movie;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class MovieRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;


    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "Age rating is required")
    private String ageRating;

    @NotBlank(message = "Director is required")
    private String director;

    private List<String> actors;

    private String trailerUrl;

    private List<String> galleryImages;

    private MultipartFile cover;
}
