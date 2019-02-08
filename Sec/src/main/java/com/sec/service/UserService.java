package com.sec.service;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.sec.entity.Role;
import com.sec.entity.User;

@Service
public interface UserService {
	
	User findByEmail(String email);

	String registerUser(User userToRegister);
	
	String userActivation(String code);

	void switchLastTicketCategory(User loggedInUser, String selectedCategory);

	ArrayList<String> rolesToList(Set<Role> roles);

	void switchLastSelectedRole(User loggedInUser, String selectedRole);
	
}
