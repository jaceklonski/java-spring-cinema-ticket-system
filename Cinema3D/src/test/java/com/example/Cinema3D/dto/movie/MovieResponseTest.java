package com.example.Cinema3D.dto.movie;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MovieResponseTest {

    @Test
    void shouldCreateMovieResponseAndReturnFields() {
        MovieResponse response = new MovieResponse(
                1L,
                "Matrix",
                136,
                "Sci-Fi classic",
                "/covers/matrix.jpg",
                "Sci-Fi",
                "16+",
                "Wachowski",
                List.of("Keanu Reeves"),
                "trailer-url",
                List.of("img1", "img2")
        );

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Matrix");
        assertThat(response.getDurationMinutes()).isEqualTo(136);
        assertThat(response.getCoverPath()).isEqualTo("/covers/matrix.jpg");
    }
}
