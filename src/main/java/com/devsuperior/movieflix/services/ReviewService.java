package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private AuthService authService;
	
	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		User user = authService.authenticated();
		
		Movie movie = movieRepository.findById(dto.getMovieId()).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
		
		Review review = new Review();
		review.setText(dto.getText());
		review.setMovie(movie);
		review.setUser(user);
		
		review = reviewRepository.save(review);
		
		return new ReviewDTO(review);
	}
	
	
}
