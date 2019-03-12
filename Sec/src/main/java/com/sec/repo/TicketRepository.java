package com.sec.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Category;
import com.sec.entity.Ticket;
import com.sec.entity.User;

public interface TicketRepository extends CrudRepository<Ticket, Long>{

	List<Ticket> findAll();
	
	Optional<Ticket> findById(Long id);

	List<Ticket> findByStatus(String status);
	
	List<Ticket> findByRequestorAndStatus(User requestor, String status);
	
	List<Ticket> findBySolverAndStatus(String solver, String status);

	List<Ticket> findByRequestor(User requestor);

	List<Ticket> findBySolver(String solver);

	List<Ticket> findBySolverAndStatusAndCategory(String solver, String status, Category selectedCategory);

	List<Ticket> findByStatusAndCategory(String status, Category selectedCategory);
	
//	@Query(value = "SELECT t FROM Tickets t WHERE t.requestor = ?1 OR t.solver = ?2 OR t.solver is null AND t.category in ?3")
	Set<Ticket> findByRequestorOrSolverOrSolverIsNullAndCategoryIn(User requestor, String solver, List<Category> categories);
	
}
