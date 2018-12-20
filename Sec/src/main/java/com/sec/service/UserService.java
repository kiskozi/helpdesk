package com.sec.service;

import org.springframework.stereotype.Service;

import com.sec.entity.User;

@Service
public interface UserService {
	
	User findByEmail(String email);

	String registerUser(User userToRegister);
	
	String userActivation(String code);
	
}
