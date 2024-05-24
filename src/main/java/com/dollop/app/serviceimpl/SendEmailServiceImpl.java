package com.dollop.app.serviceimpl;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.dollop.app.exception.ResourcesNotFoundException;
import com.dollop.app.models.EmailRequest;
import com.dollop.app.payload.EmailRequestPayload;
import com.dollop.app.repositories.EmailRepo;
import com.dollop.app.service.SendEmailService;
import com.dollop.app.util.AppConstants;
import com.dollop.app.util.EmailUtil;
import jakarta.mail.MessagingException;

@Service
public class SendEmailServiceImpl implements SendEmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailRepo emailRepo;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Override
	public void sendEmail(EmailRequestPayload emailRequestPayload) {
		
	    emailRequestPayload.setDateTime(LocalDateTime.now());
	    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	    simpleMailMessage.setFrom("dancecreation03@gmail.com");
	    simpleMailMessage.setSubject(emailRequestPayload.getEmailSubject());

	    Date date = Date.from(emailRequestPayload.getDateTime().atZone(ZoneId.systemDefault()).toInstant());
        simpleMailMessage.setFrom(env.getProperty("spring.mail.username"));
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
	    String formattedDateTime = dateFormat.format(date);
	    System.out.println("Formatted Date and Time: " + formattedDateTime);
	    String emailMessage = emailRequestPayload.getMessage() + "\n\nSent at: " + formattedDateTime;
	    
	    simpleMailMessage.setText(emailMessage);
	    simpleMailMessage.setTo(emailRequestPayload.getSendTo().toArray(new String[0]));
         
         if (emailRequestPayload.getCc() != null) {
	    	System.err.println(emailRequestPayload.getCc());
	        simpleMailMessage.setCc(emailRequestPayload.getCc().toArray(new String[0]));
	    }

	    if (emailRequestPayload.getBcc() != null) {
	        simpleMailMessage.setBcc(emailRequestPayload.getBcc().toArray(new String[0]));
	    }

	    for (String email : emailRequestPayload.getSendTo()) {
	        String emailUsername = extractUsername(email);
	        emailRequestPayload.setRecipientUsername(emailUsername);
	        System.out.println("Username for email " + email + ": " + emailUsername);
	    }
	    
	    EmailRequest emailRequest = this.emailRequestPayloadToemailRequest(emailRequestPayload);
	    emailRequest.setEmailFrom("dancecreation03@gmail.com");
	    System.err.println( emailRequest.getEmailFrom()+"get email");
	    emailRepo.save(emailRequest);
	    try {
	        mailSender.send(simpleMailMessage);
	        System.err.println("Email has been sent");
	    } catch (MailException e) {
	        System.err.println("Failed to send email: " + e.getMessage());
	    }
	}
	public String extractUsername(String email) {
	    if (email == null || !email.contains("@")) {
	        return null;
	    }
	    return email.substring(0, email.indexOf('@'));
	}
	public EmailRequest emailRequestPayloadToemailRequest(EmailRequestPayload emailRequestPayload)
	{
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setBcc(emailRequestPayload.getBcc());
		emailRequest.setCc(emailRequestPayload.getCc());
		emailRequest.setDateTime(emailRequestPayload.getDateTime());
		emailRequest.setStatus(emailRequestPayload.getStatus());
		emailRequest.setFileName(emailRequestPayload.getFileName());
		emailRequest.setId(emailRequestPayload.getId());
		emailRequest.setMessage(emailRequestPayload.getMessage());
		emailRequest.setRecipientUsername(emailRequestPayload.getRecipientUsername());
		emailRequest.setSendTo(emailRequestPayload.getSendTo());
		emailRequest.setEmailSubject(emailRequestPayload.getEmailSubject());
		return emailRequest;
	}
	public EmailRequestPayload emailRequestToEmailRequestPayload(EmailRequest emailRequest)
	{
		EmailRequestPayload emailRequestPayload = new EmailRequestPayload();
		emailRequestPayload.setBcc(emailRequest.getBcc());
		emailRequestPayload.setCc(emailRequest.getCc());
		emailRequestPayload.setDateTime(emailRequest.getDateTime());
		emailRequestPayload.setStatus(emailRequest.getStatus());
		emailRequestPayload.setFileName(emailRequest.getFileName());
		emailRequestPayload.setId(emailRequest.getId());
		emailRequestPayload.setMessage(emailRequest.getMessage());
		emailRequestPayload.setRecipientUsername(emailRequest.getRecipientUsername());
		emailRequestPayload.setSendTo(emailRequest.getSendTo());
		emailRequestPayload.setEmailSubject(emailRequest.getEmailSubject());
		return emailRequestPayload;
	}
	@Override
	public void sendEmailWithHtml(EmailRequestPayload emailRequestPayload) throws MessagingException {
		System.err.println(emailRequestPayload);
		 emailRequestPayload.setDateTime(LocalDateTime.now());
		    for (String email : emailRequestPayload.getSendTo()) {
		        String emailUsername = extractUsername(email);
		        emailRequestPayload.setRecipientUsername(emailUsername);
		        System.out.println("Username for email " + email + ": " + emailUsername);
		    }
		    EmailRequest emailRequest = this.emailRequestPayloadToemailRequest(emailRequestPayload);
		    	  emailUtil.sendEmailWithHtml(emailRequestPayload);
		    emailRequest.setEmailFrom("dancecreation03@gmail.com");
		    emailRepo.save(emailRequest);	
	}
	@Override
	public void sendEmailWithFile(EmailRequestPayload emailRequestPayload, MultipartFile file) throws IOException, MessagingException {
		   emailRequestPayload.setDateTime(LocalDateTime.now());
		    EmailRequest emailRequest = this.emailRequestPayloadToemailRequest(emailRequestPayload);          
	        emailRequest.setFileName(emailRequestPayload.getFileName());
	        for (String email : emailRequestPayload.getSendTo()) {
		        String emailUsername = extractUsername(email);
		        emailRequestPayload.setRecipientUsername(emailUsername);
		        System.out.println("Username for email " + email + ": " + emailUsername);
		    }
	        emailRequest.setEmailFrom("dancecreation03@gmail.com");
	        emailRepo.save(emailRequest);
	        emailUtil.sendEmailWithFile(emailRequest, file);
	}
	@Override
	public List<EmailRequest> findAllInboxEmail() {
		return emailRepo.findByEmailFromOrSendToOrCcOrBcc("dancecreation03@gmail.com");
	}
	
	@Override
	public List<EmailRequest> findAllSentEmail()
	{
		return emailRepo.findAll();
	}
	
	@Override
	public List<EmailRequestPayload> findALlEmailByFavorite()
	{
		List<EmailRequest> emailRequest = emailRepo.findByStatusIsTrue();
		System.err.println(emailRequest);
		List<EmailRequestPayload> emailRequestPayloads = new ArrayList<>();
		for(EmailRequest email:emailRequest)
		{
		    EmailRequestPayload emailRequestPayload = emailRequestToEmailRequestPayload(email);
		    emailRequestPayloads.add(emailRequestPayload);
		}
		return emailRequestPayloads;
	}
	@Override
	public Map<String, Object> updateFavorite(Integer id, Boolean favorite) {
		Map<String,Object> map = new HashMap<>();
	    EmailRequest emailRequest = emailRepo.findById(id)
	                                 .orElseThrow(() -> new ResourcesNotFoundException(AppConstants.USERNOTFOUND));
	    if (!emailRequest.getStatus().equals(favorite)) {
	        emailRequest.setStatus(favorite);
	        emailRepo.save(emailRequest);
	        map.put("msg", "Status updated successfully");
	 
	    } else {
	    	map.put("msg", "Status is already " + (favorite ? "active" : "deactivated"));
	    }
	    return map;
	}
	@Override
	public EmailRequestPayload getEmail(Integer id) {
		EmailRequest email = emailRepo.findById(id).orElseThrow(()-> new ResourcesNotFoundException(AppConstants.EMAILNOTFOUND));
		
		EmailRequestPayload emailRequestPayload = this.emailRequestToEmailRequestPayload(email);
		
		return emailRequestPayload;
	}
	@Override
	public List<EmailRequest> searchByEmailFields(String keyword,String email) {
        return emailRepo.findByKeywordAndLoggedInUser(keyword,email);
    }
	@Override
	public Map<String, Object> findAllEmailStatus() {
		boolean allStatusesTrueOrFalse = emailRepo.allStatusesTrueOrFalse();
		Map<String,Object> map = new HashMap<>();
		map.put("status", allStatusesTrueOrFalse);
		return map;
	}
}