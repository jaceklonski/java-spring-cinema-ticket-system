package com.example.Cinema3D.mapper;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
import com.example.Cinema3D.entity.Movie;

public class MovieMapper {

    public static Movie toEntity(MovieRequest request) {
        Movie movie = new Movie();

        movie.setTitle(request.getTitle());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setDescription(request.getDescription());

        movie.setGenre(request.getGenre());
        movie.setAgeRating(request.getAgeRating());
        movie.setDirector(request.getDirector());
        movie.setActors(request.getActors());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setGalleryImages(request.getGalleryImages());

        return movie;
    }

    public static MovieResponse toDto(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationMinutes(),
                movie.getDescription(),
                movie.getCoverUrl(),
                movie.getGenre(),
                movie.getAgeRating(),
                movie.getDirector(),
                movie.getActors(),
                movie.getTrailerUrl(),
                movie.getGalleryImages()
        );
    }
}
