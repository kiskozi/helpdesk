package com.sec.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Long>{

	List<Ticket> findAll();
	
	Optional<Ticket> findById(Long id);
	
}
