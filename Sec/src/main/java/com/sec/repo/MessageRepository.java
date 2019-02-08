package com.sec.repo;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Message;
import com.sec.entity.Ticket;

public interface MessageRepository extends CrudRepository<Message, Long> {
	
	Ticket findByMessagesTicket(Ticket ticket);

}
