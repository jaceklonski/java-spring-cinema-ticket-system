package com.example.Cinema3D.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Użytkownik
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Seans
    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    // Miejsca przypisane do tej rezerwacji
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<ScreeningSeat> screeningSeats = new HashSet<>();

    // Data utworzenia
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Liczba miejsc
    public int getSeatsCount() {
        return screeningSeats.size();
    }
}
