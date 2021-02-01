package com.web.project.service.hr;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private JavaMailSender javaMailSender;
	
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendMail(String toEmail, String subject, String message) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		
		helper.setFrom("SSAFY"); // 보내는 사람
		helper.setTo(toEmail); // 받는 사람
		helper.setSubject(subject); // 메일의 제목
		helper.setText(message, true); // true옵션을 추가할 경우, 메일을 보낼 때 html 문법을 적용!
		
		javaMailSender.send(mimeMessage);
	}
}
