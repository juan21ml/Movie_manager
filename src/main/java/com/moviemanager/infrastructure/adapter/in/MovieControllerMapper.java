package com.moviemanager.infrastructure.adapter.in;

import com.moviemanager.domain.model.Movie;
import com.moviemanager.infrastructure.adapter.in.dto.MovieRequestDto;
import com.moviemanager.infrastructure.adapter.in.dto.MovieResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper entre Movie (dominio) y DTOs del controlador
 */
@Component
public class MovieControllerMapper {

    /**
     * Convierte de Movie (dominio) a MovieResponseDto
     */
    public MovieResponseDto toResponseDto(Movie movie) {
        if (movie == null) {
            return null;
        }

        return new MovieResponseDto(
                movie.getId(),
                movie.getTmdbId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getReleaseDate(),
                movie.getVoteAverage(),
                movie.getVoteCount(),
                movie.getPosterPath(),
                movie.getBackdropPath(),
                movie.getGenres(),
                movie.getIsFavorite(),
                movie.getOriginalLanguage(),
                movie.getPopularity()
        );
    }

    /**
     * Convierte de MovieRequestDto a Movie (dominio)
     */
    public Movie toDomain(MovieRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setTmdbId(requestDto.getTmdbId());
        movie.setTitle(requestDto.getTitle());
        movie.setOverview(requestDto.getOverview());
        movie.setReleaseDate(requestDto.getReleaseDate());
        movie.setVoteAverage(requestDto.getVoteAverage());
        movie.setVoteCount(requestDto.getVoteCount());
        movie.setPosterPath(requestDto.getPosterPath());
        movie.setBackdropPath(requestDto.getBackdropPath());
        movie.setGenres(requestDto.getGenres());
        movie.setIsFavorite(requestDto.getIsFavorite());
        movie.setOriginalLanguage(requestDto.getOriginalLanguage());
        movie.setPopularity(requestDto.getPopularity());

        return movie;
    }

    /**
     * Convierte lista de Movie a lista de MovieResponseDto
     */
    public List<MovieResponseDto> toResponseDtoList(List<Movie> movies) {
        if (movies == null) {
            return List.of();
        }

        return movies.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}