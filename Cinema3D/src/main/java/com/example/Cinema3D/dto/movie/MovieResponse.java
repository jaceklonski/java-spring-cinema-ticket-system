package com.example.Cinema3D.dto.movie;

public class MovieResponse {

    private Long id;
    private String title;
    private int durationMinutes;

    public MovieResponse(Long id, String title, int durationMinutes) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
