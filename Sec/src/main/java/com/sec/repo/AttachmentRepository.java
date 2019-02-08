package com.sec.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sec.entity.Attachment;
import com.sec.entity.Ticket;

public interface AttachmentRepository extends CrudRepository<Attachment, Long> {

	List<Attachment> findByOwnerTicket(Ticket ticket);
	
}
