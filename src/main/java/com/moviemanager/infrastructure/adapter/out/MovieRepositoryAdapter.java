package com.moviemanager.infrastructure.adapter.out;

import com.moviemanager.domain.model.Movie;
import com.moviemanager.domain.port.out.MovieRepositoryPort;
import com.moviemanager.infrastructure.entity.MovieEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa MovieRepositoryPort usando JPA
 */
@Repository
public class MovieRepositoryAdapter implements MovieRepositoryPort {

    private final MovieJpaRepository jpaRepository;
    private final MovieMapper mapper;

    public MovieRepositoryAdapter(MovieJpaRepository jpaRepository, MovieMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Movie save(Movie movie) {
        MovieEntity entity = mapper.toEntity(movie);
        MovieEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Movie> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> findByIsFavoriteTrue() {
        return jpaRepository.findByIsFavoriteTrue()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Movie> findByTmdbId(Integer tmdbId) {
        return jpaRepository.findByTmdbId(tmdbId)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByTmdbId(Integer tmdbId) {
        return jpaRepository.existsByTmdbId(tmdbId);
    }
}