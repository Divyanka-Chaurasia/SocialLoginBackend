package com.dollop.app.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.models.EmailRequest;
import com.dollop.app.payload.EmailRequestPayload;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;

public interface SendEmailService {

	//send email to single person
	void sendEmail(EmailRequestPayload emailRequestPayload);
		
	//send email with html
	void sendEmailWithHtml(EmailRequestPayload emailRequestPayload) throws MessagingException;
	
	void sendEmailWithFile(EmailRequestPayload emailRequestPayload,MultipartFile file) throws IOException, MessagingException;

	List<EmailRequest> findAllInboxEmail();

	Map<String, Object> updateFavorite(Integer id, Boolean favorite);

	List<EmailRequestPayload> findALlEmailByFavorite();

	EmailRequestPayload getEmail(Integer id);

	List<EmailRequest> searchByEmailFields(String keyword,String email);

	List<EmailRequest> findAllSentEmail();
	
	Map<String, Object> findAllEmailStatus();
}
