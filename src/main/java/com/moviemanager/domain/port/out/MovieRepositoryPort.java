package com.moviemanager.domain.port.out;

import com.moviemanager.domain.model.Movie;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Movie
 */
public interface MovieRepositoryPort {
    Movie save(Movie movie);
    Optional<Movie> findById(Long id);
    List<Movie> findAll();
    List<Movie> findByIsFavoriteTrue();
    Optional<Movie> findByTmdbId(Integer tmdbId);
    void deleteById(Long id);
    boolean existsByTmdbId(Integer tmdbId);
}