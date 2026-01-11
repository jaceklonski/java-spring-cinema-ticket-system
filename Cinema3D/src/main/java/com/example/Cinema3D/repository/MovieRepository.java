package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
