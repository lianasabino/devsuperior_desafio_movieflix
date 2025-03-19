package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.movieflix.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT obj FROM Movie obj WHERE (:genreIds IS NULL OR obj.genre.id IN :genreIds) ORDER BY obj.title")
    Page<Movie> searchMoviesWithGenres(List<Long> genreIds, Pageable pageable);
}
