package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void shouldSaveMovie() {
        Movie m = new Movie();
        m.setTitle("M1");
        m.setDurationMinutes(90);
        Movie saved = movieRepository.save(m);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldUpdateMovie() {
        Movie m = new Movie();
        m.setTitle("M2");
        m.setDurationMinutes(80);
        Movie saved = movieRepository.save(m);

        saved.setTitle("M2 updated");
        Movie updated = movieRepository.save(saved);
        assertThat(updated.getTitle()).isEqualTo("M2 updated");
    }

    @Test
    void shouldDeleteMovie() {
        Movie m = new Movie();
        m.setTitle("ToDelete");
        Movie saved = movieRepository.save(m);

        movieRepository.delete(saved);
        assertThat(movieRepository.findById(saved.getId())).isEmpty();
    }
}
