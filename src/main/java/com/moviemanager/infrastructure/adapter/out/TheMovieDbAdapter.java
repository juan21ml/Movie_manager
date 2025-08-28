package com.moviemanager.infrastructure.adapter.out;

import com.moviemanager.domain.model.Movie;
import com.moviemanager.domain.port.out.ExternalMovieApiPort;
import com.moviemanager.infrastructure.adapter.out.dto.TmdbMovieDto;
import com.moviemanager.infrastructure.adapter.out.dto.TmdbMovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa ExternalMovieApiPort usando TheMovieDB API
 */
@Component
public class TheMovieDbAdapter implements ExternalMovieApiPort {

    private final RestTemplate restTemplate;
    private final TmdbMovieMapper tmdbMapper;

    @Value("${themoviedb.api.key}")
    private String apiKey;

    @Value("${themoviedb.api.base-url}")
    private String baseUrl;

    public TheMovieDbAdapter(TmdbMovieMapper tmdbMapper) {
        this.restTemplate = new RestTemplate();
        this.tmdbMapper = tmdbMapper;
    }

    @Override
    public List<Movie> searchMovies(String query) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("query", query)
                    .queryParam("language", "es-ES")
                    .build()
                    .toUriString();

            TmdbMovieResponse response = restTemplate.getForObject(url, TmdbMovieResponse.class);

            if (response != null && response.getResults() != null) {
                return tmdbMapper.toDomainList(response.getResults());
            }

            return Arrays.asList();

        } catch (Exception e) {
            System.err.println("Error searching movies: " + e.getMessage());
            return Arrays.asList();
        }
    }

    @Override
    public Optional<Movie> getMovieDetails(Integer tmdbId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/" + tmdbId)
                    .queryParam("api_key", apiKey)
                    .queryParam("language", "es-ES")
                    .build()
                    .toUriString();

            TmdbMovieDto movieDto = restTemplate.getForObject(url, TmdbMovieDto.class);

            if (movieDto != null) {
                Movie movie = tmdbMapper.toDomain(movieDto);
                return Optional.of(movie);
            }

            return Optional.empty();

        } catch (Exception e) {
            System.err.println("Error getting movie details for ID " + tmdbId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Movie> getPopularMovies() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/popular")
                    .queryParam("api_key", apiKey)
                    .queryParam("language", "es-ES")
                    .queryParam("page", 1)
                    .build()
                    .toUriString();

            TmdbMovieResponse response = restTemplate.getForObject(url, TmdbMovieResponse.class);

            if (response != null && response.getResults() != null) {
                return tmdbMapper.toDomainList(response.getResults());
            }

            return Arrays.asList();

        } catch (Exception e) {
            System.err.println("Error getting popular movies: " + e.getMessage());
            return Arrays.asList();
        }
    }

    @Override
    public List<Movie> getTopRatedMovies() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/top_rated")
                    .queryParam("api_key", apiKey)
                    .queryParam("language", "es-ES")
                    .queryParam("page", 1)
                    .build()
                    .toUriString();

            TmdbMovieResponse response = restTemplate.getForObject(url, TmdbMovieResponse.class);

            if (response != null && response.getResults() != null) {
                return tmdbMapper.toDomainList(response.getResults());
            }

            return Arrays.asList();

        } catch (Exception e) {
            System.err.println("Error getting top rated movies: " + e.getMessage());
            return Arrays.asList();
        }
    }
}