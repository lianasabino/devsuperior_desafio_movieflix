package com.devsuperior.movieflix.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
//	@Transactional(readOnly = true)
//	public Page<MovieCardDTO> FindByGenre(String genreId, Pageable pageable) {
//		
//		List<Long> genreIds = new ArrayList<>();
//
//		if(!"0".equals(genreId)) {
//			String[] vet = genreId.split(",");
//			List<String> list = Arrays.asList(vet);
//			genreIds = list.stream().map(x -> Long.parseLong(x)).toList();		
//		}
//		
//		Page<Movie> obj = movieRepository.searchMoviesWithGenres(genreIds, pageable);
//		
//		
//	}
	
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findByGenre(Pageable pageable) {
		Page<Movie> list = movieRepository.findAll(pageable);
		return list.map(x -> new MovieCardDTO(x));
	}
	
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findByGenre(String genreId, Pageable pageable) {
	    List<Long> genreIds = new ArrayList<>();

	    if (!"0".equals(genreId)) {
	        genreIds = Arrays.stream(genreId.split(","))
	                         .map(Long::parseLong)
	                         .toList();
	    }

	    Page<Movie> page;

	    if (genreIds.isEmpty()) {
	        page = movieRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("title")));
	    } else {
	        page = movieRepository.searchMoviesWithGenres(genreIds, pageable);
	    }

	    return page.map(MovieCardDTO::new);
	}



	@Transactional(readOnly = true)
	public MovieDetailsDTO findById(Long id) {
		Optional<Movie> obj = movieRepository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
		GenreDTO genreDTO = new GenreDTO(entity.getGenre());
		return new MovieDetailsDTO(entity, genreDTO);
	}
	
}
