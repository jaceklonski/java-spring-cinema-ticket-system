package com.example.Cinema3D.repository;

import com.example.Cinema3D.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findByMovieId(Long movieId);
}
