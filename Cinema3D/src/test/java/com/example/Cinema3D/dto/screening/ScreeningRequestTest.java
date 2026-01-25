package com.example.Cinema3D.dto.screening;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ScreeningRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForCorrectRequest() {
        ScreeningRequest request = new ScreeningRequest();
        request.setStartTime(LocalDateTime.now());
        request.setRoomId(1L);
        request.setMovieId(2L);

        Set<ConstraintViolation<ScreeningRequest>> violations =
                validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailWhenFieldsAreNull() {
        ScreeningRequest request = new ScreeningRequest();

        Set<ConstraintViolation<ScreeningRequest>> violations =
                validator.validate(request);

        assertThat(violations).isNotEmpty();
    }
}
