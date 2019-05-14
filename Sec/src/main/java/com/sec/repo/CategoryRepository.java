package com.sec.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	List<Category> findAll();
	
	List<Category> findAllByOrderByCategoryAsc();
	
	Optional<Category> findById(Long categoryId);
	
	Category findByCategory(String category);
	
}
