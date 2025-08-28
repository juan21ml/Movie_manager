package com.moviemanager.domain.service;

import com.moviemanager.domain.model.Movie;
import com.moviemanager.domain.port.in.MovieService;
import com.moviemanager.domain.port.out.ExternalMovieApiPort;
import com.moviemanager.domain.port.out.MovieRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de dominio para Movie
 * Contiene toda la lógica de negocio
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepositoryPort movieRepository;
    private final ExternalMovieApiPort externalMovieApi;

    public MovieServiceImpl(MovieRepositoryPort movieRepository,
                            ExternalMovieApiPort externalMovieApi) {
        this.movieRepository = movieRepository;
        this.externalMovieApi = externalMovieApi;
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> findFavorites() {
        return movieRepository.findByIsFavoriteTrue();
    }

    @Override
    public Movie updateMovie(Long id, Movie movieUpdate) {
        return movieRepository.findById(id)
                .map(existingMovie -> {
                    // Actualizar campos
                    existingMovie.setTitle(movieUpdate.getTitle());
                    existingMovie.setOverview(movieUpdate.getOverview());
                    existingMovie.setReleaseDate(movieUpdate.getReleaseDate());
                    existingMovie.setVoteAverage(movieUpdate.getVoteAverage());
                    existingMovie.setPosterPath(movieUpdate.getPosterPath());
                    existingMovie.setIsFavorite(movieUpdate.getIsFavorite());

                    return movieRepository.save(existingMovie);
                })
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    @Override
    public void deleteMovie(Long id) {
        if (!movieRepository.findById(id).isPresent()) {
            throw new RuntimeException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    public Movie addToFavorites(Integer tmdbId) {
        // Verificar si ya existe en favoritos
        Optional<Movie> existingMovie = movieRepository.findByTmdbId(tmdbId);

        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            movie.setIsFavorite(true);
            return movieRepository.save(movie);
        }

        // Si no existe, obtener de la API externa y guardarlo como favorito
        return externalMovieApi.getMovieDetails(tmdbId)
                .map(movie -> {
                    movie.setIsFavorite(true);
                    return movieRepository.save(movie);
                })
                .orElseThrow(() -> new RuntimeException("Movie not found in external API with tmdbId: " + tmdbId));
    }

    @Override
    public void removeFromFavorites(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        movie.setIsFavorite(false);
        movieRepository.save(movie);
    }

    @Override
    public List<Movie> searchMoviesFromAPI(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return externalMovieApi.searchMovies(query);
    }

    @Override
    public Movie getMovieDetailsFromAPI(Integer tmdbId) {
        return externalMovieApi.getMovieDetails(tmdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found in external API with tmdbId: " + tmdbId));
    }

    @Override
    public List<Movie> getPopularMoviesFromAPI() {
        return externalMovieApi.getPopularMovies();
    }

    @Override
    public List<Movie> getTopRatedMoviesFromAPI() {
        return externalMovieApi.getTopRatedMovies();
    }
}