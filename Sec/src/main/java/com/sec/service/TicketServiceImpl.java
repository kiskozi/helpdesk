package com.sec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Category;
import com.sec.entity.Role;
import com.sec.entity.Ticket;
import com.sec.entity.User;
import com.sec.repo.CategoryRepository;
import com.sec.repo.RoleRepository;
import com.sec.repo.TicketRepository;
import com.sec.repo.UserRepository;

@Service
public class TicketServiceImpl implements TicketService {

	private TicketRepository ticketRepository;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private CategoryRepository categoryRepository;
	
	private final String TICKET_STATUS_WAITING = "Megoldóra vár";
	private final String TICKET_STATUS_IN_PROGRESS = "Folyamatban";
	private final String TICKET_STATUS_CLOSED = "Lezárt";
	
	@Autowired
	public TicketServiceImpl(TicketRepository ticketRepository,
							UserRepository userRepository,
							RoleRepository roleRepository,
							CategoryRepository categoryRepository) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.categoryRepository = categoryRepository;
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
	public List<Ticket> findByStatusAndCategory(String status, Category selectedCategory){
		return ticketRepository.findByStatusAndCategory(status, selectedCategory);
	}
	
	@Override
	public List<Ticket> findBySolverAndStatusAndCategory(String solver, String status, Category selectedCategory) {
		return ticketRepository.findBySolverAndStatusAndCategory(solver, status, selectedCategory);
	}
	
	@Override
	public Set<Ticket> findByRequestorOrSolverOrSolverIsNullAndCategoryIn(User requestor, String solver, List<Category> categories) {
		return ticketRepository.findByRequestorOrSolverOrSolverIsNullAndCategoryIn(requestor, solver, categories);
	}

	@Override
	public List<Ticket> categorySelector(User user) {
		
		List<Ticket> tickets = new ArrayList<>();
		Set<Role> userRoles = userRepository.findUserRolesInnerJoin(user.getId());
		if (user.getSelectedCategory() != null) {
			if (user.getSelectedCategory().equals("Saját")) {
				tickets = findByRequestorAndStatus(user, user.getSelectedStatus());
			} else if (userRoles.contains(roleRepository.findByRole("ADMIN"))) {
				tickets = findByStatusAndCategory(
						user.getSelectedStatus(),
						categoryRepository.findByCategory(user.getSelectedCategory())
						);
			} else {
				tickets = findBySolverAndStatusAndCategory(
					user.getSelectedStatus().equals(TICKET_STATUS_WAITING) ? null : user.getFullName(),
					user.getSelectedStatus(),
					categoryRepository.findByCategory(user.getSelectedCategory())
					);
			}
		}
//		switch (user.getLastSelectedRole()) {
//		case "Bejelentő":
//			tickets = findByRequestorAndStatus(user, user.getSelectedStatus());
//			break;
//		case "Megoldó":
//			if (user.getSelectedStatus().equals(TICKET_STATUS_WAITING)) {
//				tickets = findBySolverAndStatus(null , user.getSelectedStatus());
//			} else {
//				tickets = findBySolverAndStatus(user.getFullName(), user.getSelectedStatus());
//			}
//			break;
//		case "Adminisztrátor":
//			tickets = findByStatus(user.getSelectedStatus());
//			break;
//		default:
//    		System.out.println("nincs kiválasztva");
//    		break;
//		}
		return tickets;
	}
	
	@Override
	public Ticket findInAllowedTickets(User user, Long id) {

		Set<Ticket> tickets = new HashSet<Ticket>();
		
		Set<Role> userRoles = userRepository.findUserRolesInnerJoin(user.getId());
		
		if (userRoles.contains(roleRepository.findByRole("ADMIN"))) {
			tickets.addAll(findAll());
		} else if (userRoles.contains(roleRepository.findByRole("SOLVER"))) {
			tickets.addAll(findByRequestorOrSolverOrSolverIsNullAndCategoryIn(
					user,
					user.getFullName(),
					userRepository.findUserCategoriesInnerJoin(user.getId())));
		} else if (userRoles.contains(roleRepository.findByRole("USER"))) {
			tickets.addAll(findByRequestor(user));
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
	public String addNewTicket(Ticket ticket, User user, Category category) {
		ticket.setRequestor(user);
		ticket.setCreated(new Date());
		ticket.setStatus(TICKET_STATUS_WAITING);
		ticket.setCategory(category);
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
