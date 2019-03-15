package com.sec.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="tickets")
public class Ticket {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	Kérelmező
//	teszt alatt String majd User
	@ManyToOne
	private User requestor;
	
	private String subject;
	
	@Column(columnDefinition = "TEXT")
//	@Column(length = 1000)
	private String description;
	
	private Date created;
	
//	megoldó
//	teszt alatt String majd User
	private String solver;
	
	private Date enrolled;
	
	private String status;
	
	private Date closed;
	
	@OneToMany(mappedBy= "ownerTicket")
	private Set<Attachment> attachments = new HashSet<Attachment>();
	
	@OneToOne(mappedBy= "messagesTicket")
	private Message message;
	
	@ManyToOne
	private Category category;
	
	public Ticket() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getRequestor() {
		return requestor;
	}

	public void setRequestor(User requestor) {
		this.requestor = requestor;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getSolver() {
		return solver;
	}

	public void setSolver(String solver) {
		this.solver = solver;
	}

	public Date getEnrolled() {
		return enrolled;
	}

	public void setEnrolled(Date enrolled) {
		this.enrolled = enrolled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getClosed() {
		return closed;
	}

	public void setClosed(Date closed) {
		this.closed = closed;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	

}
