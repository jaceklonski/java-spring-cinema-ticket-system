package com.example.Cinema3D.dto.screening;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ScreeningRequest {

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private Long roomId;

    @NotNull
    private Long movieId;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
