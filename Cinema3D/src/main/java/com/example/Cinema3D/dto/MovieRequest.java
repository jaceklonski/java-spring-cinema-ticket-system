package com.example.Cinema3D.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class MovieRequest {

    @NotBlank
    private String title;

    @Positive
    private int durationMinutes;

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
