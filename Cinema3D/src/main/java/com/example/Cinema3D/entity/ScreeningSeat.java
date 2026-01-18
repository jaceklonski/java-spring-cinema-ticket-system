package com.example.Cinema3D.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "screening_seat",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"screening_id", "seat_id"})
        }
)
public class ScreeningSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Seans
    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    // Fizyczne miejsce
    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    // Rezerwacja (null = miejsce wolne)
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // Kto zarezerwował
    @ManyToOne
    @JoinColumn(name = "reserved_by_id")
    private User reservedBy;

    // Status miejsca
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.FREE;

    // Kiedy zarezerwowano
    private LocalDateTime reservedAt;

    // Typ biletu
    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    private TicketType ticketType;
}
