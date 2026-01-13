package com.example.Cinema3D.dto.booking;

import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private Long screeningId;
    private String movieTitle;
    private int seatsCount;
    private String customerName;
    private LocalDateTime createdAt;

    public BookingResponse(Long id, Long screeningId, String movieTitle,
                           int seatsCount, String customerName, LocalDateTime createdAt) {
        this.id = id;
        this.screeningId = screeningId;
        this.movieTitle = movieTitle;
        this.seatsCount = seatsCount;
        this.customerName = customerName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getScreeningId() { return screeningId; }
    public String getMovieTitle() { return movieTitle; }
    public int getSeatsCount() { return seatsCount; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
