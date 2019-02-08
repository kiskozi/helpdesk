package com.sec.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sec.entity.Attachment;
import com.sec.entity.Ticket;

@Service
public interface AttachmentService {

	List<Attachment> findByOwnerTicket(Ticket ticket);

	String upload(Attachment attachment, Ticket ticket, MultipartFile uploadedFile);

	Resource loadAsResource(String ticketid, String filename);
	
}
