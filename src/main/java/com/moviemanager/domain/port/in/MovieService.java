package com.moviemanager.domain.port.in;

import com.moviemanager.domain.model.Movie;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para los casos de uso de Movie
 */
public interface MovieService {

    // CRUD Operations
    Movie saveMovie(Movie movie);
    Optional<Movie> findById(Long id);
    List<Movie> findAll();
    List<Movie> findFavorites();
    Movie updateMovie(Long id, Movie movie);
    void deleteMovie(Long id);

    // Business Operations
    Movie addToFavorites(Integer tmdbId);
    void removeFromFavorites(Long id);

    // External API Operations
    List<Movie> searchMoviesFromAPI(String query);
    Movie getMovieDetailsFromAPI(Integer tmdbId);
    List<Movie> getPopularMoviesFromAPI();
    List<Movie> getTopRatedMoviesFromAPI();
}