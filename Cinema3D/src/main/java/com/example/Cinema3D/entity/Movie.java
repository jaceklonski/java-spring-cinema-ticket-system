package com.example.Cinema3D.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tytuł
    @Column(nullable = false)
    private String title;

    // Czas trwania
    private int durationMinutes;

    // Opis
    @Column(length = 2000)
    private String description;

    // Okładka
    @Column(name = "cover_path")
    private String coverUrl;

    // Gatunek
    private String genre;

    // Ograniczenie wiekowe
    private String ageRating;

    // Reżyser
    private String director;

    // Obsada
    @ElementCollection
    @CollectionTable(
            name = "movie_cast",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "actor")
    private List<String> actors;

    // Trailer (YouTube embed / link)
    private String trailerUrl;

    // Galeria zdjęć
    @ElementCollection
    @CollectionTable(
            name = "movie_gallery",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "image_url")
    private List<String> galleryImages;

    // === KOMPATYBILNOŚĆ Z TEMPLATE'AMI ===
    @Transient
    public String getCoverPath() {
        return coverUrl;
    }
}
