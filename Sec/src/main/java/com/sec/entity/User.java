package com.sec.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@Email
	@Column(unique=true, nullable=false)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String confirmPassword;
	
	private String fullName;
	
	private String activation;
	
	private Boolean enabled;
	
	private String lastTicketCategory;
	
	private String lastSelectedRole;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_roles",
			joinColumns = {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="role_id")}
	)
	private Set<Role> roles = new HashSet<Role>();
	
	@OneToMany(mappedBy= "requestor")
	private Set<Ticket> tickets = new HashSet<Ticket>();
	
	public User() {
		
	}
	
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
		System.out.println("User létrejött");
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getActivation() {
		return activation;
	}

	public void setActivation(String activation) {
		this.activation = activation;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getLastTicketCategory() {
		return lastTicketCategory;
	}

	public void setLastTicketCategory(String lastTicketCategory) {
		this.lastTicketCategory = lastTicketCategory;
	}

	public String getLastSelectedRole() {
		return lastSelectedRole;
	}

	public void setLastSelectedRole(String lastSelectedRole) {
		this.lastSelectedRole = lastSelectedRole;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRoles(String roleName) {
		if (this.roles == null || this.roles.isEmpty()) {
			this.roles = new HashSet<>();
		}
		this.roles.add(new Role(roleName));
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}
	
}
