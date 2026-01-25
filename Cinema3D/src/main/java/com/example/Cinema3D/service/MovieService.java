package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.movie.MovieRequest;
import com.example.Cinema3D.dto.movie.MovieResponse;
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
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Value("${app.upload.movies:uploads/movies}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public Page<MovieResponse> getAll(Pageable pageable) {
        return movieRepository.findAll(pageable).map(MovieMapper::toDto);
    }

    @Transactional(readOnly = true)
    public MovieResponse getByIdDto(Long id) {
        return movieRepository.findById(id)
                .map(MovieMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
    }

    @Transactional
    public MovieResponse create(MovieRequest request) {
        Movie movie = MovieMapper.toEntity(request);

        if (request.getCover() != null && !request.getCover().isEmpty()) {
            movie.setCoverUrl(saveCover(request.getCover()));
        }

        return MovieMapper.toDto(movieRepository.save(movie));
    }

    @Transactional
    public MovieResponse update(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setDescription(request.getDescription());

        if (request.getCover() != null && !request.getCover().isEmpty()) {
            movie.setCoverUrl(saveCover(request.getCover()));
        }

        return MovieMapper.toDto(movieRepository.save(movie));
    }

    @Transactional
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie not found");
        }
        movieRepository.deleteById(id);
    }

    private String saveCover(MultipartFile file) {
        try {
            Path root = Paths.get(uploadDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), root.resolve(fileName));
            return "/uploads/movies/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not save file", e);
        }
    }
}