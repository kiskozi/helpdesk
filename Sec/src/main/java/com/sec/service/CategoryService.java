package com.sec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sec.entity.Category;

@Service
public interface CategoryService {
	
	List<Category> findAllByOrderByCategoryAsc();
	
	List<Category> findAllByOrderByCategoryAscRemoveOne(String toRemove);
	
	Optional <Category> findById(Long categoryId);
	
//	Category findById(String categoryId);
	
	Category findByCategory(String category);
	
}
