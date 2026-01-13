package com.example.Cinema3D.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "seats",
    uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "row_number", "seat_number"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_number", nullable = false)
    private int row;

    @Column(name = "seat_number", nullable = false)
    private int number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private CinemaRoom room;
}
