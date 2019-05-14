package com.sec.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
	
//	private EmailService emailService;
	
	private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	private final String USER_ROLE = "USER";
	
	private final String SOLVER_ROLE = "SOLVER";
	
	private final String ADMIN_ROLE = "ADMIN";
	
	private final String TICKETSTATUS = "Megoldóra vár";
	
	private final String TICKETCATEGORY = "Saját";
	
	@Autowired
	public UserServiceImpl( UserRepository userRepository,
							RoleRepository roleRepository,
							CategoryRepository categoryRepository,
							EmailService emailService
							) {
		
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.categoryRepository = categoryRepository;
//		this.emailService = emailService;
	}
	
	@Override
	public List<User> findAllByOrderByFullNameAsc() {
		return userRepository.findAllByOrderByFullNameAsc();
	}
	
	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
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
	public Set<Role> findUserPossibleRoles(Long loggedInUserId){
		return userRepository.findUserPossibleRoles(loggedInUserId);
	}
	
	@Override
	public List<Category> findUserCategoriesInnerJoin(Long loggedInUserId) {
		return userRepository.findUserCategoriesInnerJoin(loggedInUserId);
	}
	
	@Override
	public List<Category> findUserPossibleCategories(Long loggedInUserId) {
		return userRepository.findUserPossibleCategories(loggedInUserId);
	}
	
	//Ha megoldó vagy admin
	@Override
	public List<User> userSearch(String fullName, String email, String address, String phoneNumber) {
		return userRepository.findByFullNameContainingAndEmailContainingAndAddressContainingAndPhoneNumberContainingAllIgnoreCase(fullName, email, address, phoneNumber);
	
//			return userRepository.findByFullNameLikeAndEmailLikeAndAddressLikeAndPhoneNumberLikeAllIgnoreCase(
//					(fullName!=null && fullName!="") ? "%" + fullName + "%" : "%%"
//					,(email!=null && email!="") ? "%" + email + "%" : "%%"
//					,(address!=null && address!="") ? "%" + address + "%" : "%%"
//					,(phoneNumber!=null && phoneNumber!="") ? "%" + phoneNumber + "%" : "%%"
//					);
	}
	
	@Override
	public List<User> userSearch(String fullName, String email, String address, String phoneNumber, String selectedCategory) {
		return userRepository.userSearch("%" + fullName + "%", "%" + email + "%", "%" + address + "%", "%" + phoneNumber + "%", selectedCategory);
	}
	
	@Override
	public List<User> findByCategoryId(Long selectedCategoryId) {
		return userRepository.findByCategoryId(selectedCategoryId);
	}
	
	@Override
	public List<User> findCategoryPossibleUsers(Long selectedCategoryId) {
		return userRepository.findCategoryPossibleUsers(selectedCategoryId);
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
	public String editUser(User loggedInUser,String newName,String newEmail,String newAddress,String newPhoneNumber) {
		User oldUser = userRepository.findByEmail(loggedInUser.getEmail());
		User newUser = userRepository.findByEmail(newEmail);
		boolean emailChanged = !oldUser.equals(newUser);
		if (emailChanged) {
			if (newUser != null) {
				return "alreadyExists";
			}
		}
		loggedInUser.setFullName(newName);
		loggedInUser.setEmail(newEmail);
		loggedInUser.setAddress(newAddress);
		loggedInUser.setPhoneNumber(newPhoneNumber);
		userRepository.save(loggedInUser);
		return emailChanged ? "emailChanged" : "ok";
	}
	
	@Override
	public void switchSelectedStatus(User loggedInUser, String selectedStatus) {
		loggedInUser.setSelectedStatus(selectedStatus);
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

	@Override
	public String changePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
		
		if (!encoder.matches(oldPassword, user.getPassword())) return "A régi jelszó nem megfelelő!";
		
		if (!confirmPassword.equals(newPassword)) return "A jelszavak nem egyeznek!";
		
//		if (newPassword.length() < 8) return "Az új jelszónak minimum 8 karakternek kell lennie, tartalmaznia kell legalább egy kisbetűt, nagybetűt és számot!";
		
		user.setPassword(encoder.encode(newPassword));
		user.setConfirmPassword(encoder.encode(""));
		userRepository.save(user);
		
		return "A jelszó megváltozott";
	}

	@Override
	public String changePassword(User user, String newPassword, String confirmPassword) {
		
		if (!confirmPassword.equals(newPassword)) return "A jelszavak nem egyeznek!";
		
//		if (newPassword.length() < 8) return "Az új jelszónak minimum 8 karakternek kell lennie, tartalmaznia kell legalább egy kisbetűt, nagybetűt és számot!";
		
		user.setPassword(encoder.encode(newPassword));
		user.setConfirmPassword(encoder.encode(""));
		userRepository.save(user);
		
		return "A jelszó megváltozott";
	}

	@Override
	public void addCategoryToUserCategories(User selectedUser, String categoryToAdd) {
		
		if (selectedUser.getCategories().size() == 1) {
			selectedUser.getRoles().add(roleRepository.findByRole(SOLVER_ROLE));
		}
		
		selectedUser.getCategories().add(categoryRepository.findByCategory(categoryToAdd));
		userRepository.save(selectedUser);
	}

	@Override
	public void removeCategoryFromUserCategories(User selectedUser, String categoryToRemove) {
		
		selectedUser.getCategories().remove(categoryRepository.findByCategory(categoryToRemove));
		selectedUser.setSelectedCategory(TICKETCATEGORY);
		
		if (selectedUser.getCategories().size() == 1) {
			selectedUser.getRoles().remove(roleRepository.findByRole(SOLVER_ROLE));
		}
		
		userRepository.save(selectedUser);
	}

	@Override
	public void addRoleToUserRoles(User selectedUser, String roleToAdd) {
		selectedUser.getRoles().add(roleRepository.findByRole(roleToAdd));
		userRepository.save(selectedUser);
	}

	@Override
	public void removeRoleFromUserRoles(User selectedUser, String roleToRemove) {
		selectedUser.getRoles().remove(roleRepository.findByRole(roleToRemove));
		userRepository.save(selectedUser);
	}
	
	@Override
	public void disableUser(User selectedUser, String disableUser) {
//		boolean b;
//		if (disableUser.equals("true")) {
//			b = true;
//		} else if (disableUser.equals("false")) {
//			b = false;
//		} else return;
//		selectedUser.setEnabled(b);
		switch (disableUser) {
			case "true" :
				selectedUser.setEnabled(true);
				break;
			case "false" :
				selectedUser.setEnabled(false);
				break;
			default : return;
		}
		
		userRepository.save(selectedUser);
	}
	
	@Override
	public boolean userIsAdmin(User selectedUser) {
		return selectedUser.getRoles().contains(roleRepository.findByRole(ADMIN_ROLE));
	}
	
	@Override
	public void changeAdminRole(User selectedUser, String adminRole) {
		switch (adminRole) {
			case "false" :
				selectedUser.getRoles().addAll(roleRepository.findAll());
				selectedUser.getCategories().addAll(categoryRepository.findAll());
				break;
			case "true" :
				selectedUser.getRoles().remove(roleRepository.findByRole(ADMIN_ROLE));
				break;
			default : return;
		}
		userRepository.save(selectedUser);
	}
	
}
