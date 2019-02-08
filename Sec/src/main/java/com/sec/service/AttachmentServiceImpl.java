package com.sec.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sec.entity.Attachment;
import com.sec.entity.Ticket;
import com.sec.repo.AttachmentRepository;

@Service
public class AttachmentServiceImpl implements AttachmentService {
	
	private AttachmentRepository attachmentRepository;
	
	public static final String UPLOADINGDIR = System.getProperty("user.dir") + "/uploadingdir/";

	@Autowired
	public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}

	@Override
	public List<Attachment> findByOwnerTicket(Ticket ticket) {
		return attachmentRepository.findByOwnerTicket(ticket);
	}

	@Override
	public String upload(Attachment attachment, Ticket ticket, MultipartFile uploadedFile) {
		
		if (uploadedFile.isEmpty()) return "fail";
		
		String dirPath = UPLOADINGDIR + ticket.getId() + "/";
		String fileName = StringUtils.cleanPath(uploadedFile.getOriginalFilename());
		if (fileName.contains("/")) {
			String[] fileNameArray = fileName.split("/");
			fileName = fileNameArray[fileNameArray.length-1];
		}
		new File(dirPath).mkdirs();
		File file = new File(dirPath + fileName);
		
        try {
			uploadedFile.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			System.out.println("Hiba a mentéskor.");
			e.printStackTrace();
			return "fail";
		}
        
        attachment.setOwnerTicket(ticket);
		attachment.setPath(fileName);
        attachmentRepository.save(attachment);
        
		return "ok";
	}

	@Override
	public Resource loadAsResource(String ticketid, String filename) {
		Path file = Paths.get(UPLOADINGDIR + ticketid, filename);
		System.out.println(file.toString());
		
		Resource resource;
		try {
			resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) 
	            return resource;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        
        
		
		return null;
	}

}
