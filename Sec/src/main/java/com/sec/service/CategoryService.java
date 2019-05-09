package com.sec.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sec.entity.Category;

@Service
public interface CategoryService {
	
	List<Category> findAllByOrderByCategoryAsc();
	
	List<Category> findAllByOrderByCategoryAscRemoveOne(String toRemove);
	
	Category findByCategory(String category);
	
}
