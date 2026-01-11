package com.example.Cinema3D.dto.screening;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ScreeningRequest {

    @NotNull
    private LocalDateTime startTime;

    @NotBlank
    private String room;

    @NotNull
    private Long movieId;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getRoom() {
        return room;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
