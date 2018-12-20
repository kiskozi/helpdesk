package com.sec.controller;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sec.entity.Ticket;
import com.sec.entity.User;
import com.sec.repo.TicketRepository;
import com.sec.service.TicketService;
import com.sec.service.UserDetailsImpl;
//import com.sec.service.EmailService;
import com.sec.service.UserService;
import com.sec.validator.UserValidator;

import antlr.StringUtils;

@Controller
public class HomeController {
	
//	private EmailService emailService;
	
	private UserService userService;
	
	private TicketService ticketService;
	
//	@Autowired
//	public void setJavaMailSender(EmailService emailService) {
//		this.emailService = emailService;
//	}
	
	@Autowired
//	@Qualifier("")
	public void setUserService(UserService userService, TicketService ticketService) {
		this.userService = userService;
		this.ticketService = ticketService;
	}
	
	private UserValidator userValidator;
	
	@Autowired
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("ticketIsSelected", false);
		model.addAttribute("tickets", ticketService.findAll());
		return "incidents";
	}
	
	@RequestMapping("/incidents")
	public String incidents(Model model, Authentication authentication,
							@RequestParam(value = "id", required=false) String ticketId,
							@RequestParam(value="close", required=false) String ticketClose,
							@RequestParam(value="sendback", required=false) String ticketSendBack,
							@RequestParam(value="enroll", required=false) String ticketEnroll) {
		
		model.addAttribute("tickets", ticketService.findAll());
		Optional<Ticket> ticket = ticketService.findById(ticketService.idToLong(ticketId));
		if (!ticket.isPresent()) {
			model.addAttribute("ticketIsSelected", false);
			return "incidents";
		}
		Ticket selectedTicket = ticket.get();
		model.addAttribute("ticketIsSelected", true);
		model.addAttribute("selectedTicket", selectedTicket);
		
		
		
		if (ticketClose != null) {
			System.out.println("lezárás");
			ticketService.closeTicket(selectedTicket);
		} else if (ticketEnroll != null) {
			System.out.println("felvesz");
			ticketService.enrollTicket(selectedTicket, ((UserDetailsImpl) authentication.getPrincipal()).getFullName());
		} else if (ticketSendBack != null) {
			System.out.println("visszaküld");
			ticketService.sendBack(selectedTicket);
		}
		
		return "incidents";
	}
	
	
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/bloggers")
	public String bloggers() {
		return "bloggers";
	}
	
//	@Secured("ROLE_USER")
	@RequestMapping("/stories")
	public String stories() {
		return "stories";
	}
	
	@RequestMapping("/newticket")
//	public String newticket(Model model, @AuthenticationPrincipal User user) {
//	public String newticket(Model model) {
	public String newticket(Model model, Authentication authentication) {

		model.addAttribute("ticket", new Ticket());
		
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		model.addAttribute("ticketRequestor", userDetailsImpl.getFullName());
		
		return "newticket";
	}
	
	@PostMapping("/newtick")
	public String addTicket(@ModelAttribute Ticket ticket, Authentication authentication) {
//	public String addTicket(@ModelAttribute Ticket ticket) {
		System.out.println("UJ TICKET");
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		System.out.println("UserDetailsImpl: " + userDetailsImpl.getUsername());
		User user = userService.findByEmail(userDetailsImpl.getUsername());
		System.out.println(ticket.getSubject());
		ticketService.addNewTicket(ticket, user);

		return "redirect:/incidents?ticketAdded";
	}
	
	@RequestMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
//	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	@PostMapping("/reg")
	public String GreatingSubmit(@ModelAttribute User user, BindingResult bindingResult) {
		userValidator.validate(user, bindingResult);
		
		if (bindingResult.hasErrors()) {
            return "registration";
        }
		
		System.out.println("UJ USER");
//		emailService.sendMessage(user.getEmail());
		System.out.println(user.getFullName());
		System.out.println(user.getEmail());
		String newUser = userService.registerUser(user);
		System.out.println(newUser);
//		return "auth/login";
		return "redirect:/login?regsuccess";
	}
	
	
	
	
//	@RequestMapping(value = "/activation/{code}", method = RequestMethod.GET)
	@GetMapping(value = "/activation/{code}")
	public String activation(@PathVariable("code") String code, HttpServletResponse response) {
		String userAct = userService.userActivation(code);
//		return "auth/login";
		return "redirect:/login?" + userAct;
	}
	
	
	
//	@Secured("ROLE_ADMIN")
//	@RequestMapping("/delete")
//	public String delete() {
//		return "Delete";
//	}
	
}
