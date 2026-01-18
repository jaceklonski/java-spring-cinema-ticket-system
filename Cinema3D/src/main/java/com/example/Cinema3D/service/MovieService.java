package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.entity.Movie;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.mapper.MovieMapper;
import com.example.Cinema3D.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Value("${app.upload.movies}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public Page<Movie> getAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
    }

    @Transactional
    public Movie create(MovieRequest request) {
        Movie movie = MovieMapper.toEntity(request);

        if (request.getCover() != null && !request.getCover().isEmpty()) {
            movie.setCoverPath(saveCover(request.getCover()));
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public Movie update(Long id, MovieRequest request) {
        Movie movie = getById(id);

        movie.setTitle(request.getTitle());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setDescription(request.getDescription());

        if (request.getCover() != null && !request.getCover().isEmpty()) {
            movie.setCoverPath(saveCover(request.getCover()));
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie not found");
        }
        movieRepository.deleteById(id);
    }

    // =========================
    // FILE UPLOAD
    // =========================
    private String saveCover(MultipartFile file) {
        try {
            Files.createDirectories(Path.of(uploadDir));

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path target = Path.of(uploadDir).resolve(filename);

            Files.copy(file.getInputStream(), target);

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save cover", e);
        }
    }
}
