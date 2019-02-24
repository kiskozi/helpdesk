package com.sec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Ticket;
import com.sec.entity.User;
import com.sec.repo.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

	private TicketRepository ticketRepository;
	
	private final String TICKET_STATUS_WAITING = "Megoldóra vár";
	private final String TICKET_STATUS_IN_PROGRESS = "Folyamatban";
	private final String TICKET_STATUS_CLOSED = "Lezárt";
	
	@Autowired
	public TicketServiceImpl(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}
	
	@Override
	public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}
	
	@Override
	public List<Ticket> findByStatus(String status) {
		return ticketRepository.findByStatus(status);
	}
	
	@Override
	public List<Ticket> findByRequestorAndStatus(User requestor, String status) {
		return ticketRepository.findByRequestorAndStatus(requestor, status);
	}
	
	@Override
	public List<Ticket> findBySolverAndStatus(String solver, String status){
		return ticketRepository.findBySolverAndStatus(solver, status);
	}
	
	@Override
	public List<Ticket> findByRequestor(User requestor){
		return ticketRepository.findByRequestor(requestor);
	}
	
	@Override
	public List<Ticket> findBySolver(String solver){
		return ticketRepository.findBySolver(solver);
	}

	@Override
	public List<Ticket> categorySelector(User user) {
		
		List<Ticket> tickets = new ArrayList<>();
		
		switch (user.getLastSelectedRole()) {
		case "Bejelentő":
			tickets = findByRequestorAndStatus(user, user.getLastTicketCategory());
			break;
		case "Megoldó":
			if (user.getLastTicketCategory().equals(TICKET_STATUS_WAITING)) {
				tickets = findBySolverAndStatus(null , user.getLastTicketCategory());
			} else {
				tickets = findBySolverAndStatus(user.getFullName(), user.getLastTicketCategory());
			}
			break;
		case "Adminisztrátor":
			tickets = findByStatus(user.getLastTicketCategory());
			break;
		default:
    		System.out.println("nincs kiválasztva");
    		break;
		}
		return tickets;
	}
	
	@Override
	public Ticket findInAllowedTickets(User user, Long id) {
		
		List<Ticket> tickets = new ArrayList<>();
		
		switch (user.getLastSelectedRole()) {
		case "Bejelentő":
			tickets = findByRequestor(user);
			break;
		case "Megoldó":
			tickets = findBySolver(null);
			tickets.addAll(findBySolver(user.getFullName()));
			break;
		case "Adminisztrátor":
			tickets = findAll();
			break;
		default:
    		System.out.println("nincs kiválasztva");
    		break;
		}
		for (Ticket ticket : tickets) {
			if (ticket.getId() == id) return ticket;
		}
		return null;
	}
	
	@Override
	public Optional<Ticket> findById(Long id) {
		return ticketRepository.findById(id);
	}

	@Override
	public Long idToLong(String ticketId) {
		Long toLong = 0L;
		if ( ticketId != null && ticketId.trim() != "" ) {
			try {
				toLong = Long.parseLong(ticketId);
			} catch (NumberFormatException e) {}
		}
		return toLong;
	}
	
	@Override
	public String addNewTicket(Ticket ticket, User user) {
		ticket.setRequestor(user);
		ticket.setCreated(new Date());
		ticket.setStatus(TICKET_STATUS_WAITING);
		ticketRepository.save(ticket);
		return null;
	}

	@Override
	public void closeTicket(Ticket selectedTicket) {
		selectedTicket.setClosed(new Date());
		selectedTicket.setStatus(TICKET_STATUS_CLOSED);
		ticketRepository.save(selectedTicket);
	}

	@Override
	public void enrollTicket(Ticket selectedTicket, String fullName) {
		selectedTicket.setEnrolled(new Date());
		selectedTicket.setSolver(fullName);
		selectedTicket.setStatus(TICKET_STATUS_IN_PROGRESS);
		ticketRepository.save(selectedTicket);
	}

	@Override
	public void sendBack(Ticket selectedTicket) {
		selectedTicket.setEnrolled(null);
		selectedTicket.setSolver(null);
		selectedTicket.setStatus(TICKET_STATUS_WAITING);
		ticketRepository.save(selectedTicket);
	}

	
	


}
