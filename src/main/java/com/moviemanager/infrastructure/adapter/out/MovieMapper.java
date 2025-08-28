package com.moviemanager.infrastructure.adapter.out;

import com.moviemanager.domain.model.Movie;
import com.moviemanager.infrastructure.entity.MovieEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper entre Movie (dominio) y MovieEntity (infraestructura)
 */
@Component
public class MovieMapper {

    /**
     * Convierte de MovieEntity a Movie (dominio)
     */
    public Movie toDomain(MovieEntity entity) {
        if (entity == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setId(entity.getId());
        movie.setTmdbId(entity.getTmdbId());
        movie.setTitle(entity.getTitle());
        movie.setOverview(entity.getOverview());
        movie.setReleaseDate(entity.getReleaseDate());
        movie.setVoteAverage(entity.getVoteAverage());
        movie.setVoteCount(entity.getVoteCount());
        movie.setPosterPath(entity.getPosterPath());
        movie.setBackdropPath(entity.getBackdropPath());
        movie.setIsFavorite(entity.getIsFavorite());
        movie.setOriginalLanguage(entity.getOriginalLanguage());
        movie.setPopularity(entity.getPopularity());

        // Convertir string de géneros a lista
        if (entity.getGenres() != null && !entity.getGenres().isEmpty()) {
            List<String> genresList = Arrays.asList(entity.getGenres().split(","));
            movie.setGenres(genresList.stream()
                    .map(String::trim)
                    .collect(Collectors.toList()));
        }

        return movie;
    }

    /**
     * Convierte de Movie (dominio) a MovieEntity
     */
    public MovieEntity toEntity(Movie movie) {
        if (movie == null) {
            return null;
        }

        MovieEntity entity = new MovieEntity();
        entity.setId(movie.getId());
        entity.setTmdbId(movie.getTmdbId());
        entity.setTitle(movie.getTitle());
        entity.setOverview(movie.getOverview());
        entity.setReleaseDate(movie.getReleaseDate());
        entity.setVoteAverage(movie.getVoteAverage());
        entity.setVoteCount(movie.getVoteCount());
        entity.setPosterPath(movie.getPosterPath());
        entity.setBackdropPath(movie.getBackdropPath());
        entity.setIsFavorite(movie.getIsFavorite());
        entity.setOriginalLanguage(movie.getOriginalLanguage());
        entity.setPopularity(movie.getPopularity());

        // Convertir lista de géneros a string
        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            String genresString = String.join(", ", movie.getGenres());
            entity.setGenres(genresString);
        }

        return entity;
    }

    /**
     * Actualiza una entidad existente con datos de Movie
     */
    public void updateEntity(MovieEntity entity, Movie movie) {
        if (entity == null || movie == null) {
            return;
        }

        entity.setTmdbId(movie.getTmdbId());
        entity.setTitle(movie.getTitle());
        entity.setOverview(movie.getOverview());
        entity.setReleaseDate(movie.getReleaseDate());
        entity.setVoteAverage(movie.getVoteAverage());
        entity.setVoteCount(movie.getVoteCount());
        entity.setPosterPath(movie.getPosterPath());
        entity.setBackdropPath(movie.getBackdropPath());
        entity.setIsFavorite(movie.getIsFavorite());
        entity.setOriginalLanguage(movie.getOriginalLanguage());
        entity.setPopularity(movie.getPopularity());

        // Convertir lista de géneros a string
        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            String genresString = String.join(", ", movie.getGenres());
            entity.setGenres(genresString);
        }
    }
}