package com.moviemanager.infrastructure.adapter.out;

import com.moviemanager.infrastructure.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para MovieEntity
 */
@Repository
public interface MovieJpaRepository extends JpaRepository<MovieEntity, Long> {

    List<MovieEntity> findByIsFavoriteTrue();
    Optional<MovieEntity> findByTmdbId(Integer tmdbId);
    boolean existsByTmdbId(Integer tmdbId);
}