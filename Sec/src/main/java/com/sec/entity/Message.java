package com.sec.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="messages")
public class Message {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String messageFileName;
	
	@OneToOne
	private Ticket messagesTicket;

	public Message() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageFileName() {
		return messageFileName;
	}

	public void setMessageFileName(String messageFileName) {
		this.messageFileName = messageFileName;
	}

	public Ticket getMessagesTicket() {
		return messagesTicket;
	}

	public void setMessagesTicket(Ticket messagesTicket) {
		this.messagesTicket = messagesTicket;
	}
	
	
	
}
