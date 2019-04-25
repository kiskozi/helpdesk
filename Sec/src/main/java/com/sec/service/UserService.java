package com.sec.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.sec.entity.Category;
import com.sec.entity.Role;
import com.sec.entity.User;

@Service
public interface UserService {
	
	List<User> findAllByOrderByFullNameAsc();
	
	Optional<User> findById(Long id);
	
	User findByEmail(String email);

	String registerUser(User userToRegister);
	
	String userActivation(String code);

	void switchSelectedStatus(User loggedInUser, String SelectedStatus);

	ArrayList<String> rolesToList(Set<Role> roles);

	void switchSelectedCategory(User loggedInUser, String selectedCategory);
	
	Set<Role> findUserRolesInnerJoin(Long loggedInUserId);
	
	Set<Role> findUserPossibleRoles(Long loggedInUserId);
	
	List<Category> findUserCategoriesInnerJoin(Long loggedInUserId);
	
	List<Category> findUserPossibleCategories(Long loggedInUserId);

	String editUser(User loggedInUser,String newName,String newEmail,String newAddress,String newPhoneNumber);

	Long idToLong(String userId);

	String changePassword(User user, String oldPassword, String newPassword, String confirmPassword);

	String changePassword(User user, String newPassword, String confirmPassword);

	void addCategoryToUserCategories(User selectedUser, String categoryToAdd);

	void removeCategoryFromUserCategories(User selectedUser, String categoryToRemove);

	void addRoleToUserRoles(User selectedUser, String roleToAdd);

	void removeRoleFromUserRoles(User selectedUser, String roleToRemove);

	void disableUser(User selectedUser, String disableUser);

	boolean userIsAdmin(User selectedUser);

	void changeAdminRole(User selectedUser, String adminRole);
	
}
