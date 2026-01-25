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

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ScreeningSeatRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ScreeningSeatRepository screeningSeatRepository;

    @Autowired
    private UserRepository userRepository;

    private ScreeningSeat ss;
    private User user;

    @BeforeEach
    void setUp() {
        CinemaRoom room = new CinemaRoom();
        room.setName("r");
        em.persistAndFlush(room);

        Movie movie = new Movie();
        movie.setTitle("m");
        movie.setDurationMinutes(100);
        em.persistAndFlush(movie);

        Screening screening = new Screening();
        screening.setStartTime(LocalDateTime.now().plusDays(1));
        screening.setRoom(room);
        screening.setMovie(movie);
        em.persistAndFlush(screening);

        Seat seat = new Seat();
        seat.setRow(1);
        seat.setNumber(1);
        seat.setRoom(room);
        em.persistAndFlush(seat);

        user = new User();
        user.setUsername("bob");
        user.setPassword("p");
        user.setRole(Role.USER);
        user = em.persistAndFlush(user);

        ss = new ScreeningSeat();
        ss.setScreening(screening);
        ss.setSeat(seat);
        ss.setStatus(SeatStatus.RESERVED);
        ss.setReservedAt(LocalDateTime.now());
        ss.setReservedBy(user);
        ss = em.persistAndFlush(ss);
    }

    @Test
    void shouldFindByScreeningId() {
        List<ScreeningSeat> list = screeningSeatRepository.findByScreeningId(ss.getScreening().getId());
        assertThat(list).isNotEmpty();
    }

    @Test
    void shouldFindByReservedByAndStatus() {
        List<ScreeningSeat> list = screeningSeatRepository.findByReservedByAndStatus(user, SeatStatus.RESERVED);
        assertThat(list).hasSizeGreaterThanOrEqualTo(1);
    }
}
