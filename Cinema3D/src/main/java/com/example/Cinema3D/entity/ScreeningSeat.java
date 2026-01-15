package com.example.Cinema3D.entity;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"screening_id", "seat_id"}
        )
)
public class ScreeningSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.FREE;

    /**
     * Rezerwacja / zakup, do którego należy to miejsce.
     * NULL = wolne
     */
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // ===== GETTERY =====

    public Long getId() {
        return id;
    }

    public Screening getScreening() {
        return screening;
    }

    public Seat getSeat() {
        return seat;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public Booking getBooking() {
        return booking;
    }

    // ===== SETTERY =====

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
