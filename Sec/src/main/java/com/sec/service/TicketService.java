package com.sec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sec.entity.Ticket;
import com.sec.entity.User;

@Service
public interface TicketService {

	List<Ticket> findAll();
	
	List<Ticket> findByStatus(String status);
	
	List<Ticket> findByRequestorAndStatus(User requestor, String status);
	
	List<Ticket> findBySolverAndStatus(String solver, String status);
	
	List<Ticket> findByRequestor(User requestor);
	
	List<Ticket> findBySolver(String solver);
	
	List<Ticket> categorySelector(User user);
	
//	List<Ticket> categorySelector(String selectedCategory);

	Ticket findInAllowedTickets(User user, Long id);

	Optional<Ticket> findById(Long id);

	String addNewTicket(Ticket ticket, User user);

	Long idToLong(String ticketId);

	void closeTicket(Ticket selectedTicket);

	void enrollTicket(Ticket selectedTicket, String fullName);

	void sendBack(Ticket selectedTicket);
	
}
