package com.sec.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Ticket;
import com.sec.entity.User;

public interface TicketRepository extends CrudRepository<Ticket, Long>{

	List<Ticket> findAll();
	
	Optional<Ticket> findById(Long id);

	List<Ticket> findByStatus(String status);
	
	List<Ticket> findByRequestorAndStatus(User requestor, String status);
	
	List<Ticket> findBySolverAndStatus(String solver, String status);
	
}
