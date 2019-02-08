package com.sec.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sec.entity.Role;
import com.sec.entity.User;
import com.sec.repo.RoleRepository;
import com.sec.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private EmailService emailService;
	
	private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	private final String USER_ROLE = "USER";
	
	private final String TICKETCATEGORY = "Megoldóra vár";
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.emailService = emailService;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}

	@Override
	public String registerUser(User userToRegister) {
		
		User userCheck = userRepository.findByEmail(userToRegister.getEmail());
		
		if (userCheck != null) {
			return "alreadyExists";
		}
		
		Role userRole = roleRepository.findByRole(USER_ROLE);
		if (userRole != null) {
			userToRegister.getRoles().add(userRole);
		} else {
			userToRegister.addRoles(USER_ROLE);
		}
		
		userToRegister.setPassword(encoder.encode(userToRegister.getPassword()));
		userToRegister.setConfirmPassword(encoder.encode(""));
		
		String code = generateKey();
		System.out.println("activation/" + code);
		userToRegister.setEnabled(false);
		userToRegister.setActivation(code);
		userToRegister.setLastTicketCategory(TICKETCATEGORY);
		userToRegister.setLastSelectedRole(rolesToList(userToRegister.getRoles()).get(0));
		userRepository.save(userToRegister);
//		e-mail küldés
//		emailService.sendMessage(userToRegister.getEmail(), userToRegister.getFullName(), code);
		
		return "ok";
	}
	
	public String generateKey() {
		Random random = new Random();
		char[] word = new char[16];
		for (int i = 0; i < word.length; i++) {
			word[i] = (char) ('a' + random.nextInt(26));
		}
		return new String(word);
	}

	@Override
	public String userActivation(String code) {
		User user = userRepository.findByActivation(code);
		if (user == null) {
			return "novalidcode";
		}
		user.setEnabled(true);
		user.setActivation("");
		userRepository.save(user);
		return "activationsuccess";
	}

	@Override
	public void switchLastTicketCategory(User loggedInUser, String selectedCategory) {
		loggedInUser.setLastTicketCategory(selectedCategory);
		userRepository.save(loggedInUser);
	}
	
	@Override
	public void switchLastSelectedRole(User loggedInUser, String selectedRole) {
		loggedInUser.setLastSelectedRole(selectedRole);
		userRepository.save(loggedInUser);
	}

	@Override
	public ArrayList<String> rolesToList(Set<Role> roles) {
		ArrayList<String> rolesArray = new ArrayList<>();
		for (Role role : roles)
			switch (role.getRole()) {
    			case "ADMIN": 
    				rolesArray.add("Adminisztrátor");
    				break;
				case "REQUESTOR": 
			    	rolesArray.add("Megoldó");
			    	break;
				case "USER": 
			    	rolesArray.add("Bejelentő");
			    	break;
				default:
			    	break;
			}
		Collections.sort(rolesArray);
		return rolesArray;
	}

	
	
	
	
}
