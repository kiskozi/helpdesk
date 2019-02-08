package com.sec.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="attachments")
public class Attachment {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String path;
	
	@ManyToOne
	private Ticket ownerTicket;

	public Attachment() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Ticket getOwnerTicket() {
		return ownerTicket;
	}

	public void setOwnerTicket(Ticket ownerTicket) {
		this.ownerTicket = ownerTicket;
	}
	
	
	
}
