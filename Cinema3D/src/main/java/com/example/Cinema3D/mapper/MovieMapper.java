package com.example.Cinema3D.mapper;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.entity.Movie;

public class MovieMapper {

    public static Movie toEntity(MovieRequest request) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setDescription(request.getDescription());
        return movie;
    }
}
