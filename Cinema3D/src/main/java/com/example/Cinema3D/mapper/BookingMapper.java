package com.example.Cinema3D.mapper;

import com.example.Cinema3D.dto.booking.BookingResponse;
import com.example.Cinema3D.entity.Booking;

public class BookingMapper {

    public static BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getScreening().getId(),
                booking.getScreening().getMovie().getTitle(),
                booking.getSeatsCount(),
                booking.getCreatedAt()
        );
    }
}
