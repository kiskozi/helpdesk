package com.sec.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Category;
import com.sec.entity.Role;
import com.sec.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAll();
	
	User findByEmail(String email);
	
	User findByActivation(String activation);
	
	@Query(value = "SELECT r FROM User u INNER JOIN u.roles r WHERE u.id = ?1")
	Set<Role> findUserRolesInnerJoin(Long loggedInUserId);
	
	@Query(value = "SELECT c FROM User u INNER JOIN u.categories c WHERE u.id = ?1 order by c.category asc")
	List<Category> findUserCategoriesInnerJoin(Long loggedInUserId);
	
//	select * from categories where id not in ( SELECT category_id FROM USER_CATEGORIES where user_id = 2)
	
}
