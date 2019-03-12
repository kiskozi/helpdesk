package com.sec.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.sec.entity.Category;
import com.sec.entity.Role;
import com.sec.entity.User;

@Service
public interface UserService {
	
	List<User> findAll();
	
	User findByEmail(String email);

	String registerUser(User userToRegister);
	
	String userActivation(String code);

	void switchSelectedStatus(User loggedInUser, String SelectedStatus);

	ArrayList<String> rolesToList(Set<Role> roles);

	void switchSelectedCategory(User loggedInUser, String selectedCategory);
	
	Set<Role> findUserRolesInnerJoin(Long loggedInUserId);
	
	List<Category> findUserCategoriesInnerJoin(Long loggedInUserId);
	
}
