package com.sec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sec.entity.Ticket;
import com.sec.entity.User;

@Service
public interface TicketService {

	List<Ticket> findAll();

	Optional<Ticket> findById(Long id);

	String addNewTicket(Ticket ticket, User user);

	Long idToLong(String ticketId);

	void closeTicket(Ticket selectedTicket);

	void enrollTicket(Ticket selectedTicket, String fullName);

	void sendBack(Ticket selectedTicket);
	
}
