package com.example.Cinema3D.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private CinemaRoom room;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Transient
    public LocalDateTime getEndTime() {
        if (startTime == null || movie == null) {
            return null;
        }
        return startTime.plusMinutes(movie.getDurationMinutes());
    }
}
