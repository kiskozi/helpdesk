package com.sec.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sec.entity.Category;
import com.sec.entity.Role;
import com.sec.entity.User;
import com.sec.repo.CategoryRepository;
import com.sec.repo.RoleRepository;
import com.sec.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private UserRepository userRepository;
	
	private RoleRepository roleRepository;
	
	private CategoryRepository categoryRepository;
	
	private EmailService emailService;
	
	private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	private final String USER_ROLE = "USER";
	
	private final String TICKETSTATUS = "Megoldóra vár";
	
	private final String TICKETCATEGORY = "Saját";
	
	@Autowired
	public UserServiceImpl( UserRepository userRepository,
							RoleRepository roleRepository,
							CategoryRepository categoryRepository,
							EmailService emailService ) {
		
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.categoryRepository = categoryRepository;
		this.emailService = emailService;
	}
	
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Set<Role> findUserRolesInnerJoin(Long loggedInUserId) {
		return userRepository.findUserRolesInnerJoin(loggedInUserId);
	}
	
	@Override
	public List<Category> findUserCategoriesInnerJoin(Long loggedInUserId) {
		return userRepository.findUserCategoriesInnerJoin(loggedInUserId);
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
		Category userCategory = categoryRepository.findByCategory(TICKETCATEGORY);
		if (userCategory != null) {
			userToRegister.getCategories().add(userCategory);
		} else {
			userToRegister.addCategories(TICKETCATEGORY);
		}
		userToRegister.setPassword(encoder.encode(userToRegister.getPassword()));
		userToRegister.setConfirmPassword(encoder.encode(""));
		
		String code = generateKey();
		System.out.println("activation/" + code);
		userToRegister.setEnabled(false);
		userToRegister.setActivation(code);
		userToRegister.setSelectedStatus(TICKETSTATUS);
		userToRegister.setSelectedCategory(new ArrayList<>(userToRegister.getCategories()).get(0).getCategory());
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
	public void switchSelectedStatus(User loggedInUser, String SelectedStatus) {
		loggedInUser.setSelectedStatus(SelectedStatus);
		userRepository.save(loggedInUser);
	}
	
	@Override
	public void switchSelectedCategory(User loggedInUser, String selectedCategory) {
		loggedInUser.setSelectedCategory(selectedCategory);
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
