package com.moviemanager.domain.port.out;

import com.moviemanager.domain.model.Movie;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para API externa (TheMovieDB)
 */
public interface ExternalMovieApiPort {
    List<Movie> searchMovies(String query);
    Optional<Movie> getMovieDetails(Integer tmdbId);
    List<Movie> getPopularMovies();
    List<Movie> getTopRatedMovies();
}