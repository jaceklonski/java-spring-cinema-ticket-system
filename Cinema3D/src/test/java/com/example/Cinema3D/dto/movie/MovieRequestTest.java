package com.example.Cinema3D.dto.movie;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MovieRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForValidMovieRequest() {
        MovieRequest request = new MovieRequest();
        request.setTitle("Inception");
        request.setDurationMinutes(148);
        request.setGenre("Sci-Fi");
        request.setAgeRating("PG-13");
        request.setDirector("Christopher Nolan");
        request.setActors(List.of("Leonardo DiCaprio"));
        request.setDescription("Dreams within dreams");

        Set<ConstraintViolation<MovieRequest>> violations =
                validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailWhenRequiredFieldsMissing() {
        MovieRequest request = new MovieRequest();

        Set<ConstraintViolation<MovieRequest>> violations =
                validator.validate(request);

        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .anyMatch(p -> p.toString().equals("title"))
                .anyMatch(p -> p.toString().equals("durationMinutes"))
                .anyMatch(p -> p.toString().equals("genre"))
                .anyMatch(p -> p.toString().equals("ageRating"))
                .anyMatch(p -> p.toString().equals("director"));
    }

    @Test
    void shouldFailWhenDurationIsNegative() {
        MovieRequest request = new MovieRequest();
        request.setTitle("Test");
        request.setDurationMinutes(-10);
        request.setGenre("Drama");
        request.setAgeRating("R");
        request.setDirector("Someone");

        Set<ConstraintViolation<MovieRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("durationMinutes"));
    }

    @Test
    void shouldFailWhenDescriptionTooLong() {
        MovieRequest request = new MovieRequest();
        request.setTitle("Test");
        request.setDurationMinutes(120);
        request.setGenre("Drama");
        request.setAgeRating("R");
        request.setDirector("Someone");
        request.setDescription("x".repeat(2500));

        Set<ConstraintViolation<MovieRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }
}
