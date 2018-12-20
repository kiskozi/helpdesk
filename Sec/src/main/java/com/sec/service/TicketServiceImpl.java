package com.sec.service;

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
		selectedTicket.setSolver("");
		selectedTicket.setStatus(TICKET_STATUS_WAITING);
		ticketRepository.save(selectedTicket);
	}

}
