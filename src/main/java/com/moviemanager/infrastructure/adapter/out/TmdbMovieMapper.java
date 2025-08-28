package com.moviemanager.infrastructure.adapter.out;

import com.moviemanager.domain.model.Movie;
import com.moviemanager.infrastructure.adapter.out.dto.TmdbMovieDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper para convertir DTOs de TheMovieDB a Movie (dominio)
 */
@Component
public class TmdbMovieMapper {

    // Mapa de géneros de TheMovieDB (simplificado)
    private static final Map<Integer, String> GENRE_MAP = new HashMap<>();

    static {
        GENRE_MAP.put(28, "Action");
        GENRE_MAP.put(12, "Adventure");
        GENRE_MAP.put(16, "Animation");
        GENRE_MAP.put(35, "Comedy");
        GENRE_MAP.put(80, "Crime");
        GENRE_MAP.put(99, "Documentary");
        GENRE_MAP.put(18, "Drama");
        GENRE_MAP.put(10751, "Family");
        GENRE_MAP.put(14, "Fantasy");
        GENRE_MAP.put(36, "History");
        GENRE_MAP.put(27, "Horror");
        GENRE_MAP.put(10402, "Music");
        GENRE_MAP.put(9648, "Mystery");
        GENRE_MAP.put(10749, "Romance");
        GENRE_MAP.put(878, "Science Fiction");
        GENRE_MAP.put(10770, "TV Movie");
        GENRE_MAP.put(53, "Thriller");
        GENRE_MAP.put(10752, "War");
        GENRE_MAP.put(37, "Western");
    }

    /**
     * Convierte TmdbMovieDto a Movie (dominio)
     */
    public Movie toDomain(TmdbMovieDto dto) {
        if (dto == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setTmdbId(dto.getId());
        movie.setTitle(dto.getTitle());
        movie.setOverview(dto.getOverview());
        movie.setVoteAverage(dto.getVoteAverage());
        movie.setVoteCount(dto.getVoteCount());
        movie.setPosterPath(dto.getPosterPath());
        movie.setBackdropPath(dto.getBackdropPath());
        movie.setOriginalLanguage(dto.getOriginalLanguage());
        movie.setPopularity(dto.getPopularity());
        movie.setIsFavorite(false); // Por defecto no es favorito

        // Convertir fecha
        if (dto.getReleaseDate() != null && !dto.getReleaseDate().isEmpty()) {
            try {
                LocalDate releaseDate = LocalDate.parse(dto.getReleaseDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                movie.setReleaseDate(releaseDate);
            } catch (DateTimeParseException e) {
                // Si hay error en el formato, dejar como null
                movie.setReleaseDate(null);
            }
        }

        // Convertir géneros
        if (dto.getGenreIds() != null && !dto.getGenreIds().isEmpty()) {
            List<String> genres = dto.getGenreIds().stream()
                    .map(genreId -> GENRE_MAP.getOrDefault(genreId, "Unknown"))
                    .filter(genre -> !"Unknown".equals(genre))
                    .collect(Collectors.toList());
            movie.setGenres(genres);
        }

        return movie;
    }

    /**
     * Convierte lista de TmdbMovieDto a lista de Movie
     */
    public List<Movie> toDomainList(List<TmdbMovieDto> dtoList) {
        if (dtoList == null) {
            return Arrays.asList();
        }

        return dtoList.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}