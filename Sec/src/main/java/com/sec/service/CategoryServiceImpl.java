package com.sec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Category;
import com.sec.repo.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	private CategoryRepository categoryRepository;
	
	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> findAllByOrderByCategoryAsc() {
		return categoryRepository.findAllByOrderByCategoryAsc();
	}

	@Override
	public List<Category> findAllByOrderByCategoryAscRemoveOne(String toRemove) {
		List<Category> categories = categoryRepository.findAllByOrderByCategoryAsc();
		for (Category c : categories) {
			if (c.getCategory().equals(toRemove)) {
				categories.remove(c);
			}
		}
		return categories;
	}

	@Override
	public Category findByCategory(String category) {
		return categoryRepository.findByCategory(category);
	}
	
	
	
}
