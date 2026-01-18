package com.example.Cinema3D.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MovieRequest {

    @NotBlank
    private String title;

    @NotNull
    private Integer durationMinutes;

    @NotBlank
    private String description;

    // 📸 OKŁADKA
    private MultipartFile cover;
}
