package com.moviemanager.domain.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Modelo de dominio para Película
 */
public class Movie {
    private Long id;
    private Integer tmdbId; // ID de TheMovieDB
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private Double voteAverage;
    private Integer voteCount;
    private String posterPath;
    private String backdropPath;
    private List<String> genres;
    private Boolean isFavorite;
    private String originalLanguage;
    private Double popularity;

    // Constructor vacío
    public Movie() {}

    // Constructor completo
    public Movie(Long id, Integer tmdbId, String title, String overview,
                 LocalDate releaseDate, Double voteAverage, Integer voteCount,
                 String posterPath, String backdropPath, List<String> genres,
                 Boolean isFavorite, String originalLanguage, Double popularity) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.genres = genres;
        this.isFavorite = isFavorite;
        this.originalLanguage = originalLanguage;
        this.popularity = popularity;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", tmdbId=" + tmdbId +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", voteAverage=" + voteAverage +
                ", isFavorite=" + isFavorite +
                '}';
    }
}