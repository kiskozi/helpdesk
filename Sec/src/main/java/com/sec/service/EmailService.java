package com.sec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
	
	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;
	
	@Value("${url}" + "/activation/")
	private String URL;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
//	@Autowired
//	public void setJavaMailSender(EmailService emailService) {
//		this.javaMailSender = javaMailSender;
//	}
	
	public void sendMessage(String email, String fullName, String code) {
		SimpleMailMessage message = null;
		try {
			message = new SimpleMailMessage();
			message.setFrom(MESSAGE_FROM);
			message.setTo(email);
			message.setSubject("Sikeres regisztrálás");
			message.setText("Kedves " + fullName + "!\n\n"
							+ "Köszönjük, hogy regisztrált az oldalunkra!\n"
							+ "A regisztráció aktiválásához kattintson az alábbi linkre, vagy másoljaki és illessze be a böngésző címsorába:\n"
							+ URL + code);
			javaMailSender.send(message);
		} catch (Exception e) {
			System.out.println("Hiba az e-mail küldésekor, az alábbi címre: " + email + " " + e);
		}
		
		
	}
	
	
}
