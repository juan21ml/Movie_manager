package com.moviemanager.infrastructure.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO para la respuesta de búsqueda de TheMovieDB API
 */
public class TmdbMovieResponse {

    private int page;
    private List<TmdbMovieDto> results;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

    // Constructor vacío
    public TmdbMovieResponse() {}

    // Getters y Setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<TmdbMovieDto> getResults() {
        return results;
    }

    public void setResults(List<TmdbMovieDto> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}