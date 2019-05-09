package com.sec.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Category;
import com.sec.entity.Role;
import com.sec.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAllByOrderByFullNameAsc();
	
	Optional<User> findById(Long id);
	
	User findByEmail(String email);
	
	User findByActivation(String activation);
	
	@Query(value = "SELECT r FROM User u INNER JOIN u.roles r WHERE u.id = ?1 order by r.role asc")
	Set<Role> findUserRolesInnerJoin(Long loggedInUserId);
	
	@Query(value = "SELECT r FROM Role r WHERE r.id NOT IN ( SELECT rid.id FROM User u INNER JOIN u.roles rid WHERE u.id = ?1 ) ORDER BY role ASC")
	Set<Role> findUserPossibleRoles(Long loggedInUserId);
	
	@Query(value = "SELECT c FROM User u INNER JOIN u.categories c WHERE u.id = ?1 order by c.category asc")
	List<Category> findUserCategoriesInnerJoin(Long loggedInUserId);
	
//	@Query(value = "SELECT * FROM categories WHERE id NOT IN ( SELECT category_id FROM user_categories WHERE user_id = ?1) ORDER BY category ASC", nativeQuery = true)
	@Query(value = "SELECT c FROM Category c WHERE c.id NOT IN ( SELECT cid.id FROM User u INNER JOIN u.categories cid WHERE u.id = ?1 ) ORDER BY category ASC")
	List<Category> findUserPossibleCategories(Long loggedInUserId);
	
	List<User> findByFullNameContainingAndEmailContainingAndAddressContainingAndPhoneNumberContainingAllIgnoreCase(String fullName, String email, String address, String phoneNumber);
	
//	@Query(value = "SELECT u FROM User u "
//			+ "WHERE UPPER(u.fullName) LIKE UPPER(?1) "
//			+ "AND UPPER(u.fullName) LIKE UPPER(?2) "
//			+ "AND UPPER(u.fullName) LIKE UPPER(?3) "
//			+ "AND UPPER(u.fullName) LIKE UPPER(?4) "
//			+ "AND u.id IN (SELECT uid.id FROM Category c INNER JOIN c.users uid "
//			+ "WHERE c.id = (SELECT cid.id FROM Category cid "
//			+ "WHERE cid.category = ?5)) "
//			+ "ORDER BY fullName ASC")
	@Query(value = "SELECT * FROM users WHERE UPPER(full_name) LIKE UPPER(?1) AND UPPER(email) LIKE UPPER(?2) AND UPPER(address) LIKE UPPER(?3) AND UPPER(phone_number) LIKE UPPER(?4) AND id IN (SELECT user_id FROM user_categories WHERE category_id = (SELECT id FROM categories WHERE category = ?5)) ORDER BY full_name ASC;", nativeQuery = true)
	List<User> userSearch(String fullName, String email, String address, String phoneNumber, String categoryName);
	
}
