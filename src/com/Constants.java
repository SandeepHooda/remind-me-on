package com;

import java.util.ArrayList;
import java.util.List;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.communication.phone.text.Key;

public class Constants {

	public static final long aDay = 1000*60*60*24;
	
	public static final void sendEmail(String toEmail, String subject, String message) {
		//After notification delete the reminder
		EmailVO emalVO = new EmailVO();
		emalVO.setUserName("personal.reminder.notification@gmail.com");
		emalVO.setPassword(Key.email);
		emalVO.setSubject(subject);
		emalVO.setHtmlContent(message);
		EmailAddess from = new EmailAddess();
		from.setAddress(emalVO.getUserName());
		
		List<EmailAddess> receipients = new ArrayList<>();
		EmailAddess to = new EmailAddess();
		to.setAddress(toEmail);
		emalVO.setFromAddress(from);
		receipients.add(to);
		emalVO.setToAddress(receipients);
		MailService.sendSimpleMail(emalVO);
	}
}
