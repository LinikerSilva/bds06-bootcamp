package com.devsuperior.movieflix.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository repository; 
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Transactional(readOnly = true)
	public Page<ReviewDTO> findAllPaged(Pageable pageable) {
		Page<Review> list = repository.findAll(pageable);
		return list.map(ReviewDTO::new);
	}
	
	@Transactional(readOnly = true)
	public ReviewDTO findById(Long id) {
		authService.validateSelfOrAdmin(id);
		Optional<Review> obj = repository.findById(id);
		Review entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ReviewDTO(entity);
	}

	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		Review entity = new Review();
		entity.setText(dto.getText());
		entity.setMovie(movieRepository.getOne(dto.getMovieId()));
		entity.setUser(authService.authenticated());
		entity = repository.save(entity);
		return new ReviewDTO(entity);
	}

	@Transactional
	public ReviewDTO update(Long id, ReviewDTO dto) {
		try {
		     Review entity = repository.getOne(id);
		     entity.setText(dto.getText());
		     entity.setMovie(movieRepository.getOne(dto.getMovieId()));
		     entity.setUser(authService.authenticated());
		     entity = repository.save(entity);
			 return new ReviewDTO(entity);  
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
}