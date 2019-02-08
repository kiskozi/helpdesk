package com.sec.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sec.entity.Message;
import com.sec.entity.Ticket;
import com.sec.repo.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	
	private MessageRepository messageRepository;
	
	public static final String MESSAGESDIR = System.getProperty("user.dir") + "/messages/";
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String SPLITSTRING = "/splithere/";
	
	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}
	
	@Override
	public Ticket findByMessagesTicket(Ticket ticket) {
		return messageRepository.findByMessagesTicket(ticket);
	}

	@Override
	public String createNewMessage(Ticket ticket) {
		Message message = new Message();
		message.setMessagesTicket(ticket);
		message.setMessageFileName("ticket_" + ticket.getId() + ".hdm");
		messageRepository.save(message);
		new File(MESSAGESDIR).mkdirs();
		try {
			new File(MESSAGESDIR + message.getMessageFileName()).createNewFile();
		} catch (IOException e) {
			System.out.println("Nem sikerült létrehozni a .hdm fájlt.");
			e.printStackTrace();
		}
		
		return "CreateSuccessful";
	}
	
	@Override
	public String sendMessage(Ticket selectedTicket, String loggedInUser, String messageToSend, Date messageDate) {
		Message message = selectedTicket.getMessage();
		List<String> lines = Arrays.asList(loggedInUser + ": " + SPLITSTRING + messageToSend + SPLITSTRING +"(" + DATEFORMAT.format(messageDate) + ")");
		Path messageFile = Paths.get(MESSAGESDIR + message.getMessageFileName());
		try {
			Files.write(messageFile, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println("Nem sikerült írni a .hdm fájlba.");
			e.printStackTrace();
		}
		return "MessageSent";
	}

	@Override
	public ArrayList<String[]> getMessages(Ticket selectedTicket) {
		Message message = selectedTicket.getMessage();
		Path messageFile = Paths.get(MESSAGESDIR + message.getMessageFileName());
		List<String> allMessages = new ArrayList<>();
		try {
			allMessages = Files.readAllLines(messageFile);
		} catch (IOException e) {
			System.out.println("Nem sikerült olvasni a .hdm fájlba.");
			e.printStackTrace();
		}
		if (allMessages.isEmpty()) allMessages.add(" " + SPLITSTRING + "Csevegés..." + SPLITSTRING + " ");
		ArrayList<String[]> nextLineArray = new ArrayList<String[]>();
		for (String nextLine : allMessages) nextLineArray.add(nextLine.split(SPLITSTRING));
		return nextLineArray;
	}

}
