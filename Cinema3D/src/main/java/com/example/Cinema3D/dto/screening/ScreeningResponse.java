package com.example.Cinema3D.dto.screening;

import java.time.LocalDateTime;

public class ScreeningResponse {

    private Long id;
    private LocalDateTime startTime;
    private String room;
    private Long movieId;
    private String movieTitle;

    public ScreeningResponse(Long id,
                             LocalDateTime startTime,
                             String room,
                             Long movieId,
                             String movieTitle) {
        this.id = id;
        this.startTime = startTime;
        this.room = room;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getRoom() {
        return room;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
}
