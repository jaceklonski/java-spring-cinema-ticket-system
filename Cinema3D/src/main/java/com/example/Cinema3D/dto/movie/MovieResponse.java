package com.example.Cinema3D.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

    private Long id;
    private String title;
    private int durationMinutes;
    private String description;
    private String coverUrl;
    private String genre;
    private String ageRating;
    private String director;
    private List<String> actors;
    private String trailerUrl;
    private List<String> galleryImages;

    public String getCoverPath() {
        return coverUrl;
    }
}
