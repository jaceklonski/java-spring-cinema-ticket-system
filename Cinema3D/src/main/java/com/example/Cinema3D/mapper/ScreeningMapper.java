package com.example.Cinema3D.mapper;

import com.example.Cinema3D.dto.screening.ScreeningResponse;
import com.example.Cinema3D.entity.Screening;

public class ScreeningMapper {

    public static ScreeningResponse toResponse(Screening screening) {
        return new ScreeningResponse(
                screening.getId(),
                screening.getStartTime(),
                screening.getRoom().getId(),
                screening.getRoom().getName(),
                screening.getMovie().getId(),
                screening.getMovie().getTitle()
        );
    }
}
