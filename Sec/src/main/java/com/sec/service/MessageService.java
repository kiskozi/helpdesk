package com.sec.service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.sec.entity.Ticket;

@Service
public interface MessageService {
	
	Ticket findByMessagesTicket(Ticket ticket);
	
	String createNewMessage(Ticket ticket);

	String sendMessage(Ticket selectedTicket, String loggedInUser, String messageToSend, Date messageDate);

	ArrayList<String[]> getMessages(Ticket selectedTicket);

}
