package com.sec.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.sec.entity.Attachment;
import com.sec.entity.Category;
import com.sec.entity.Role;
import com.sec.entity.Ticket;
import com.sec.entity.User;
import com.sec.repo.CategoryRepository;
import com.sec.repo.RoleRepository;
import com.sec.service.AttachmentService;
import com.sec.service.CategoryService;
import com.sec.service.MessageService;
import com.sec.service.TicketService;
import com.sec.service.UserDetailsImpl;
//import com.sec.service.EmailService;
import com.sec.service.UserService;
import com.sec.validator.UserValidator;

@Controller
public class HomeController {
	
//	private EmailService emailService;
	
	private UserService userService;
	private TicketService ticketService;
	private AttachmentService attachmentService;
	private MessageService messageService;
	private CategoryService categoryService;
	
//	@Autowired
//	public void setJavaMailSender(EmailService emailService) {
//		this.emailService = emailService;
//	}
	
	@Autowired
//	@Qualifier("")
	public void setUserService(UserService userService,
								TicketService ticketService,
								AttachmentService attachmentService,
								MessageService messageService,
								CategoryService categoryService
								) {
		
		this.userService = userService;
		this.ticketService = ticketService;
		this.attachmentService = attachmentService;
		this.messageService = messageService;
		this.categoryService = categoryService;
	}
	
	private UserValidator userValidator;
	
	@Autowired
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}
	
	@RequestMapping("/")
	public String home(Model model, Authentication authentication,
						@RequestParam(value="status", required=false) String selectedStatus,
//						@RequestParam(value="selectedRole", required=false) String selectedRole,
						@RequestParam(value="selectedCategory", required=false) String selectedCategory
						) {
		User loggedInUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		
		model.addAttribute("categories", userService.findUserCategoriesInnerJoin(loggedInUser.getId()));
//		model.addAttribute("roles", userService.rolesToList(loggedInUser.getRoles()));
		model.addAttribute("ticketIsSelected", false);
//		model.addAttribute("tickets", ticketService.findAll());
		
//		Set<Ticket> allowedTickets = ticketService.findByRequestorOrSolverOrSolverIsNullAndCategoryIn(
//				loggedInUser,
//				loggedInUser.getFullName(),
//				userService.findUserCategoriesInnerJoin(loggedInUser.getId())
//				);
//		for (Ticket t : allowedTickets) {
//			System.out.println(t.getId());
//		}
		
		
		if (selectedCategory != null) {
			userService.switchSelectedCategory(loggedInUser, selectedCategory);
		}
		model.addAttribute("lastCategory", loggedInUser.getSelectedCategory());
		
//		
//		if (selectedRole != null) {
//			userService.switchLastSelectedRole(loggedInUser, selectedRole);
//		}
//		model.addAttribute("lastRole", loggedInUser.getLastSelectedRole());
//		
		if (selectedStatus != null) {
			userService.switchSelectedStatus(loggedInUser, selectedStatus);
		}
		model.addAttribute("tickets", ticketService.categorySelector(loggedInUser));
//		model.addAttribute("tickets", ticketService.categorySelector(loggedInUser.getLastTicketCategory()));

		model.addAttribute("selectedStatus", loggedInUser.getSelectedStatus());
		
		
		return "incidents";
	}
	
//	@Secured("ROLE_ADMIN")
	@RequestMapping("/incidents")
	public String incidents(Model model, Authentication authentication,
							@RequestParam(value="id", required=false) String ticketId,
							@RequestParam(value="close", required=false) String ticketClose,
							@RequestParam(value="sendback", required=false) String ticketSendBack,
							@RequestParam(value="enroll", required=false) String ticketEnroll,
							@RequestParam(value="uploadingFiles", required=false) MultipartFile[] uploadingFiles,
							@RequestParam(value="messageToSend", required=false) String messageToSend,
							@RequestParam(value="status", required=false) String selectedStatus,
							@RequestParam(value="selectedCategory", required=false) String selectedCategory
							) {
		
//		UserDetailsImpl loggedInUser = ((UserDetailsImpl) authentication.getPrincipal());
		User loggedInUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		
		model.addAttribute("categories", userService.findUserCategoriesInnerJoin(loggedInUser.getId()));
//		model.addAttribute("roles", userService.rolesToList(loggedInUser.getRoles()));
		
		Ticket selectedTicket = ticketService.findInAllowedTickets(loggedInUser, ticketService.idToLong(ticketId));
		if (selectedTicket == null) {
			model.addAttribute("ticketIsSelected", false);
		} else {
			model.addAttribute("ticketIsSelected", true);
			model.addAttribute("selectedTicket", selectedTicket);
			
		
//			Ticket ticket = ticketService.findInAllowedTickets(loggedInUser, ticketService.idToLong(ticketId));
//			if (ticket == null) {
//				model.addAttribute("ticketIsSelected", false);
//				return "incidents";
//			}
//			Ticket selectedTicket = ticket;
//			model.addAttribute("ticketIsSelected", true);
//			model.addAttribute("selectedTicket", selectedTicket);
		
			if (ticketClose != null) {
				System.out.println("lezárás");
				ticketService.closeTicket(selectedTicket);
			} else if (ticketEnroll != null) {
				System.out.println("felvesz");
				ticketService.enrollTicket(selectedTicket, loggedInUser.getFullName());
			} else if (ticketSendBack != null) {
				System.out.println("visszaküld");
				ticketService.sendBack(selectedTicket);
			}
			
			if (uploadingFiles != null) {
				for(MultipartFile uploadedFile : uploadingFiles) {
					attachmentService.upload(new Attachment(), selectedTicket, uploadedFile);
				}
			}
			model.addAttribute("attachments", attachmentService.findByOwnerTicket(selectedTicket));
			
			if (messageToSend != null) {
				messageService.sendMessage(selectedTicket, loggedInUser.getFullName(), messageToSend, new Date());
			}
			model.addAttribute("allMessages", messageService.getMessages(selectedTicket));
		}
		
		if (selectedCategory != null) {
			userService.switchSelectedCategory(loggedInUser, selectedCategory);
		}
		model.addAttribute("lastCategory", loggedInUser.getSelectedCategory());
		
		if (selectedStatus != null) {
			userService.switchSelectedStatus(loggedInUser, selectedStatus);
		}
		model.addAttribute("tickets", ticketService.categorySelector(loggedInUser));
		model.addAttribute("selectedStatus", loggedInUser.getSelectedStatus());
		
		return "incidents";
	}
	
//	@PostMapping(value = "/ticketlist")
//	public String getTickets(Model model) {
//		
////		List<Ticket> ticketList = ticketService.findAll();
//		model.addAttribute("ticketlist", ticketService.findAll());
//		model.addAttribute("tliscreated", true);
////		return new ResponseEntity<List<Ticket>>(ticketList, HttpStatus.OK);
//		return "incidents";
//	}
	
	@GetMapping(value = "/files/{ticketid}/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("ticketid") String ticketid,
    										  @PathVariable("filename") String filename) {
		// Authentication authentication,
		// if ( ticketid = ((UserDetailsImpl) authentication.getPrincipal()).getTickets() ticket.getId())
		
		
		Resource file = attachmentService.loadAsResource(ticketid, filename);
		
        return ResponseEntity.ok()
        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        		.body(file);
		
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
	public String newticket(Model model, Authentication authentication
//							,@RequestParam(value="uploadingFiles", required=false) MultipartFile[] uploadingFiles
							) {

		
		model.addAttribute("ticket", new Ticket());
		
		User loggedInUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		model.addAttribute("ticketRequestor", loggedInUser.getFullName());
		
		List<Category> categories = categoryService.findAllByOrderByCategoryAsc();
		for (Category c : categories) {
			if (c.getCategory().equals("Saját")) {
				categories.remove(c);
			}
		}
		
		model.addAttribute("categories", categories);
		
		
		
		
//		System.out.println(uploadingFiles);
//		if (uploadingFiles != null) {
//			for(MultipartFile uploadedFile : uploadingFiles) {
//				attachmentService.upload(new Attachment(), ticket, uploadedFile);
//			}
//			System.out.println("feltöltés befejezve");
//		}
		
//		model.addAttribute("attachments", attachmentService.findByOwnerTicket(ticket));
		
		return "newticket";
	}
	
	@PostMapping("/newtick")
	public String addTicket(@ModelAttribute Ticket ticket, Authentication authentication,
							@RequestParam(value="uploadingFiles", required=false) MultipartFile[] uploadingFiles,
							@RequestParam(value="selectedCategory", required=false) String selectedCategory
							) {
		
//	public String addTicket(@ModelAttribute Ticket ticket) {
		Category category = categoryService.findByCategory(selectedCategory);
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		User user = userService.findByEmail(userDetailsImpl.getUsername());
		ticketService.addNewTicket(ticket, user, category);
		messageService.createNewMessage(ticket);
		

		if (uploadingFiles != null) {
			System.out.println(uploadingFiles);
			for(MultipartFile uploadedFile : uploadingFiles) {
				attachmentService.upload(new Attachment(), ticket, uploadedFile);
			}
			System.out.println("feltöltés befejezve");
		}
//		
//		model.addAttribute("attachments", attachmentService.findByOwnerTicket(ticket));

		return "redirect:/incidents?ticketAdded";
	}
	
//	@RequestMapping("/attachfile")
//	@ResponseBody
//	public void attachFile(Model model,
//						   @RequestParam(value="uploadingFiles", required=false) MultipartFile[] uploadingFiles) {
//		return "redirect:/newtick";
//	}
	
	@RequestMapping("/userprofile")
	public String userprofile(
								Model model,
								Authentication authentication,
//								@ModelAttribute("passwordChanged") String passwordChanged,
								@RequestParam(value="id", required=false) String userId,
								@RequestParam(value="categoryToAdd", required=false) String categoryToAdd,
								@RequestParam(value="categoryToRemove", required=false) String categoryToRemove,
								@RequestParam(value="roleToAdd", required=false) String roleToAdd,
								@RequestParam(value="roleToRemove", required=false) String roleToRemove,
								@RequestParam(value="disableUser", required=false) String disableUser,
								@RequestParam(value="adminRole", required=false) String adminRole
								) {
		model.addAttribute("users", userService.findAllByOrderByFullNameAsc());
		
		Optional<User> findUser = userService.findById(userService.idToLong(userId));
		
		User selectedUser = findUser.isPresent() ? findUser.get() : userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		
		if (categoryToAdd != null) userService.addCategoryToUserCategories(selectedUser, categoryToAdd);
		if (categoryToRemove != null) userService.removeCategoryFromUserCategories(selectedUser, categoryToRemove);
		
//		if (roleToAdd != null) userService.addRoleToUserRoles(selectedUser, roleToAdd);
//		if (roleToRemove != null) userService.removeRoleFromUserRoles(selectedUser, roleToRemove);
		
		if (disableUser != null) userService.disableUser(selectedUser, disableUser);
		
		if (adminRole != null) userService.changeAdminRole(selectedUser, adminRole);
		
		model.addAttribute("selectedUser", selectedUser);
		
		model.addAttribute("categoriesToAdd", userService.findUserPossibleCategories(selectedUser.getId()));
		
		List<Category> addedCategories = userService.findUserCategoriesInnerJoin(selectedUser.getId());
		addedCategories.remove(categoryService.findByCategory("Saját"));
		model.addAttribute("addedCategories", addedCategories);
		
//		model.addAttribute("rolesToAdd", userService.findUserPossibleRoles(selectedUser.getId()));
//		model.addAttribute("addedRoles", userService.findUserRolesInnerJoin(selectedUser.getId()));
		
		model.addAttribute("userIsAdmin", userService.userIsAdmin(selectedUser));
		
//		for (Role r : selectedUser.getRoles() ) {
//			System.out.println(r.getRole());
//		}
		
		
//		model.addAttribute("passwordChanged", passwordChanged);
		return "profile";
	}
	
	@PostMapping("/userprof")
	public String editUserProfile(Authentication authentication, HttpServletRequest request, HttpServletResponse response, User selectedUser,
			@RequestParam(value="newName", required=false) String newName,
			@RequestParam(value="newEmail", required=false) String newEmail,
			@RequestParam(value="newAddress", required=false) String newAddress,
			@RequestParam(value="newPhoneNumber", required=false) String newPhoneNumber,
			@RequestParam(value="id", required=false) String userId
			) {
		User loggedInUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		Optional<User> findUser = userService.findById(userService.idToLong(userId));
		if (findUser.isPresent()) {
			selectedUser = findUser.get();
		} else {
			selectedUser = loggedInUser;
		}
		String editProfileStatus = userService.editUser(selectedUser, newName, newEmail, newAddress, newPhoneNumber);
		if (editProfileStatus.equals("emailChanged") && loggedInUser.equals(selectedUser)) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
			return "redirect:/login?logout";
		}
		return "redirect:/userprofile?id=" + userId;
	}
	
	@RequestMapping("/profile")
	public String profile(Model model, Authentication authentication) {
		User selectedUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		model.addAttribute("selectedUser", selectedUser);
		return "profile";
	}
	
	@PostMapping("/prof")
	public String editProfile(
			Authentication authentication, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="newName", required=false) String newName,
			@RequestParam(value="newEmail", required=false) String newEmail,
			@RequestParam(value="newAddress", required=false) String newAddress,
			@RequestParam(value="newPhoneNumber", required=false) String newPhoneNumber
			) {
		User loggedInUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		
		String editProfileStatus = userService.editUser(loggedInUser, newName, newEmail, newAddress, newPhoneNumber);
		if (editProfileStatus.equals("emailChanged")) {
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			new SecurityContextLogoutHandler().logout(request, response, authentication);
			return "redirect:/login?logout";
		}
		return "redirect:/profile";
	}
	
	@PostMapping("/changeuserpass")
//	public String changeUserPassword(
	public RedirectView changeUserPassword(RedirectAttributes attributes,
			Authentication authentication,
			User selectedUser,
//			Model model,
//			@RequestParam(value="oldPassword", required=false) String oldPassword,
			@RequestParam(value="newPassword", required=false) String newPassword,
			@RequestParam(value="confirmPassword", required=false) String confirmPassword,
			@RequestParam(value="id", required=false) String userId
			) {
		User loggedInUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		Optional<User> findUser = userService.findById(userService.idToLong(userId));
		if (findUser.isPresent()) {
			selectedUser = findUser.get();
		} else {
			selectedUser = loggedInUser;
		}
		String validation = userService.changePassword(selectedUser, newPassword, confirmPassword);
//		model.addAttribute("passwordChanged", validation);
//		model.addAttribute("selectedUser", selectedUser);
//		return "redirect:/userprofile?id=" + userId;
		attributes.addFlashAttribute("passwordChanged", validation);
//		attributes.addFlashAttribute("selectedUser", selectedUser);
	    attributes.addAttribute("id", userId);
		
		return new RedirectView("userprofile");
	}
	
	@PostMapping("/changepass")
//	public String changePassword(
	public RedirectView changePassword(RedirectAttributes attributes,
			Authentication authentication,
//			Model model,
			@RequestParam(value="oldPassword", required=false) String oldPassword,
			@RequestParam(value="newPassword", required=false) String newPassword,
			@RequestParam(value="confirmPassword", required=false) String confirmPassword
			) {
		User loggedInUser = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
		String validation = userService.changePassword(loggedInUser, oldPassword, newPassword, confirmPassword);
//		model.addAttribute("passwordChanged", validation);
//		model.addAttribute("selectedUser", loggedInUser);
//		return "profile";
		
		attributes.addFlashAttribute("passwordChanged", validation);
		return new RedirectView("profile");
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
	
	@RequestMapping("/users")
	public String users(Model model) {
		return "users";
	}
	
	@RequestMapping("/categories")
	public String categories(Model model) {
		return "categories";
	}
	
}
