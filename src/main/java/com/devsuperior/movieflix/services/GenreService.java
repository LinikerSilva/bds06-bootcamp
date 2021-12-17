package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class GenreService {
	
	@Autowired
	private GenreRepository repository; 
	
	@Transactional(readOnly = true)
	public Page<GenreDTO> findAllPaged(Pageable pageable) {
		Page<Genre> list = repository.findAll(pageable);
		return list.map(GenreDTO::new);
	}
	
	public List<GenreDTO> findAll() {
		List<Genre> list = repository.findAll();
		return list.stream().map(GenreDTO::new).collect(Collectors.toList());
		
	}
	
	@Transactional(readOnly = true)
	public GenreDTO findById(Long id) {
		Optional<Genre> obj = repository.findById(id);
		Genre entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new GenreDTO(entity);
	}
}