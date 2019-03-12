package com.sec.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	List<Category> findAllByOrderByCategoryAsc();
	
	Category findByCategory(String category);
	
}
