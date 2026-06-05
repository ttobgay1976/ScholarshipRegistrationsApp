package com.sprms.registration.mailServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);

	// import repository
	private final JavaMailSender _mailSender;

	public EmailServices(JavaMailSender mailSender) {
		this._mailSender = mailSender;
	}

	// email service to send notification to applicant if application is rejected
	public void sendSuccessRegistrationEmail(String toEmail, String applicantName, Long appId, String remarks) {

		logger.info("@@@Calling the sendRejectionEmail proc...............");

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(toEmail);
		message.setSubject("SPRMS - Application Rejected");

		message.setText("Dear " + applicantName + ",\n\n" + "Your scholarship application (ID: " + appId
				+ ") has been submitted successfully.\n\n" +  "Regards,\nSPRMS Team");

		_mailSender.send(message);
	}

	// testing the mail
	public void sendTestEmail(String to) {

		logger.info("@@@Calling the sendTestEmail pro.............");
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(to);
		message.setSubject("SPRMS Test Email");
		message.setText("Hello! Email system is working in SPRMS 👍");

		_mailSender.send(message);
	}
}
