package com.moviemanager.infrastructure.adapter.in;

import com.moviemanager.domain.model.Movie;
import com.moviemanager.domain.port.in.MovieService;
import com.moviemanager.infrastructure.adapter.in.dto.MovieRequestDto;
import com.moviemanager.infrastructure.adapter.in.dto.MovieResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones de Movie
 */
@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieService movieService;
    private final MovieControllerMapper mapper;

    public MovieController(MovieService movieService, MovieControllerMapper mapper) {
        this.movieService = movieService;
        this.mapper = mapper;
    }

    /**
     * GET /api/movies - Obtener todas las películas
     */
    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<Movie> movies = movieService.findAll();
        List<MovieResponseDto> response = mapper.toResponseDtoList(movies);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/movies/{id} - Obtener película por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long id) {
        return movieService.findById(id)
                .map(movie -> ResponseEntity.ok(mapper.toResponseDto(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/movies/favorites - Obtener películas favoritas
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<MovieResponseDto>> getFavoriteMovies() {
        List<Movie> favorites = movieService.findFavorites();
        List<MovieResponseDto> response = mapper.toResponseDtoList(favorites);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/movies - Crear nueva película
     */
    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@Valid @RequestBody MovieRequestDto requestDto) {
        Movie movie = mapper.toDomain(requestDto);
        Movie savedMovie = movieService.saveMovie(movie);
        MovieResponseDto response = mapper.toResponseDto(savedMovie);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/movies/{id} - Actualizar película
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(@PathVariable Long id,
                                                        @Valid @RequestBody MovieRequestDto requestDto) {
        try {
            Movie movieUpdate = mapper.toDomain(requestDto);
            Movie updatedMovie = movieService.updateMovie(id, movieUpdate);
            MovieResponseDto response = mapper.toResponseDto(updatedMovie);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/movies/{id} - Eliminar película
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/movies/{tmdbId}/favorite - Agregar película a favoritos desde TMDB
     */
    @PostMapping("/{tmdbId}/favorite")
    public ResponseEntity<MovieResponseDto> addToFavorites(@PathVariable Integer tmdbId) {
        try {
            Movie movie = movieService.addToFavorites(tmdbId);
            MovieResponseDto response = mapper.toResponseDto(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/movies/{id}/favorite - Remover de favoritos
     */
    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long id) {
        try {
            movieService.removeFromFavorites(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/movies/search?q={query} - Buscar películas en TMDB
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieResponseDto>> searchMovies(@RequestParam("q") String query) {
        try {
            List<Movie> movies = movieService.searchMoviesFromAPI(query);
            List<MovieResponseDto> response = mapper.toResponseDtoList(movies);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/movies/popular - Obtener películas populares de TMDB
     */
    @GetMapping("/popular")
    public ResponseEntity<List<MovieResponseDto>> getPopularMovies() {
        List<Movie> movies = movieService.getPopularMoviesFromAPI();
        List<MovieResponseDto> response = mapper.toResponseDtoList(movies);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/movies/top-rated - Obtener películas mejor valoradas de TMDB
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieResponseDto>> getTopRatedMovies() {
        List<Movie> movies = movieService.getTopRatedMoviesFromAPI();
        List<MovieResponseDto> response = mapper.toResponseDtoList(movies);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/movies/tmdb/{tmdbId} - Obtener detalles de película desde TMDB
     */
    @GetMapping("/tmdb/{tmdbId}")
    public ResponseEntity<MovieResponseDto> getMovieFromTmdb(@PathVariable Integer tmdbId) {
        try {
            Movie movie = movieService.getMovieDetailsFromAPI(tmdbId);
            MovieResponseDto response = mapper.toResponseDto(movie);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}