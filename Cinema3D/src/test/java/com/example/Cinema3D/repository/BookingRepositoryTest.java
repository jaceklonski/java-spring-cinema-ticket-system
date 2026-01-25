package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.liquibase.enabled=false"
})
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    private User user;
    private Screening screening;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pw");
        user.setRole(Role.USER);
        this.user = em.persistAndFlush(user);

        CinemaRoom room = new CinemaRoom();
        room.setName("R1");
        room = em.persistAndFlush(room);

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDurationMinutes(100);
        movie = em.persistAndFlush(movie);

        Screening screening = new Screening();
        screening.setStartTime(LocalDateTime.now().plusDays(1));
        screening.setRoom(room);
        screening.setMovie(movie);
        this.screening = em.persistAndFlush(screening);
    }

    @Test
    void shouldSaveBooking() {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setCreatedAt(LocalDateTime.now());

        Booking saved = bookingRepository.saveAndFlush(booking);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldFindBookingById() {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setCreatedAt(LocalDateTime.now());
        Booking saved = bookingRepository.saveAndFlush(booking);

        Optional<Booking> found = bookingRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUsername()).isEqualTo("alice");
    }

    @Test
    void shouldFindBookingsByUser() {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setCreatedAt(LocalDateTime.now());
        bookingRepository.saveAndFlush(booking);

        List<Booking> list = bookingRepository.findByUserOrderByIdDesc(user);

        assertThat(list).hasSize(1);
    }

    @Test
    void shouldUpdateBooking() {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setCreatedAt(LocalDateTime.now());
        Booking saved = bookingRepository.saveAndFlush(booking);

        Screening other = new Screening();
        other.setStartTime(LocalDateTime.now().plusDays(2));
        other.setRoom(screening.getRoom());
        other.setMovie(screening.getMovie());
        other = em.persistAndFlush(other);

        saved.setScreening(other);
        Booking updated = bookingRepository.saveAndFlush(saved);

        assertThat(updated.getScreening().getId()).isEqualTo(other.getId());
    }

    @Test
    void shouldDeleteBooking() {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setCreatedAt(LocalDateTime.now());
        Booking saved = bookingRepository.saveAndFlush(booking);

        bookingRepository.delete(saved);
        bookingRepository.flush();

        assertThat(bookingRepository.findById(saved.getId())).isEmpty();
    }
}
