package com.example.Cinema3D.dto.screening;

import java.time.LocalDateTime;

public class ScreeningResponse {

    private Long id;
    private LocalDateTime startTime;
    private Long roomId;
    private String roomName;
    private Long movieId;
    private String movieTitle;

    public ScreeningResponse(
            Long id,
            LocalDateTime startTime,
            Long roomId,
            String roomName,
            Long movieId,
            String movieTitle
    ) {
        this.id = id;
        this.startTime = startTime;
        this.roomId = roomId;
        this.roomName = roomName;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    public Long getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public Long getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public Long getMovieId() { return movieId; }
    public String getMovieTitle() { return movieTitle; }
}
