package com.dollop.app.util;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.dollop.app.models.EmailRequest;
import com.dollop.app.payload.EmailReplyPlayload;
import com.dollop.app.payload.EmailRequestPayload;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private Environment env;
    
    public void sendEmailWithHtml(EmailRequestPayload emailRequestPayload) throws MessagingException 
    {
    	System.err.println(emailRequestPayload);
          Date date = Date.from(emailRequestPayload.getDateTime().atZone(ZoneId.systemDefault()).toInstant());
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
          String formattedDateTime = dateFormat.format(date);
          String emailMessage = emailRequestPayload.getMessage() + "\n\nSent at: " + formattedDateTime;
            
           MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper;

                helper = new MimeMessageHelper(mailMessage,true,"UTF-8");
                helper.setTo(emailRequestPayload.getSendTo().toArray(new String[0]));
                helper.setSubject(emailRequestPayload.getEmailSubject());
                String replyTo = "hello";
				helper.setReplyTo(replyTo);
                helper.setFrom("dancecreation03@gmail.com");
                helper.setText(emailMessage);
                
                if (emailRequestPayload.getCc() != null) {
                    System.err.println(emailRequestPayload.getCc());
                    helper.setCc(emailRequestPayload.getCc().toArray(new String[0]));
                }

                if (emailRequestPayload.getBcc() != null) {
                    helper.setBcc(emailRequestPayload.getBcc().toArray(new String[0]));
                }
                
               mailSender.send(mailMessage);
    }
  
    public void sendEmailWithFile(EmailRequest emailRequest, MultipartFile file) throws MessagingException
    {
    	   MimeMessage createMimeMessage = mailSender.createMimeMessage();
 
   	        MimeMessageHelper helper = new MimeMessageHelper(createMimeMessage, true);
   	        helper.setFrom("dancecreation03@gmail.com");
   	        helper.setTo(emailRequest.getSendTo().toArray(new String[0]));
   	        helper.setText(emailRequest.getMessage());
   	        helper.setSubject(emailRequest.getEmailSubject());

   	        // Add file as attachment
   	        helper.addAttachment(file.getOriginalFilename(), file);
   	        
   	        if (emailRequest.getCc() != null) {
   	            helper.setCc(emailRequest.getCc().toArray(new String[0]));
   	        }
   	        if (emailRequest.getBcc() != null) {
   	            helper.setBcc(emailRequest.getBcc().toArray(new String[0]));
   	        }    
   	        
   	     mailSender.send(createMimeMessage);
    }
    
    public void sendReplyEmailWithHtml(EmailReplyPlayload emailReplyPayload) throws MessagingException {
        Date date = Date.from(emailReplyPayload.getDateTime().atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDateTime = dateFormat.format(date);
        String emailMessage = emailReplyPayload.getMessage() + "<br><br>Sent at: " + formattedDateTime;
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
        helper.setTo(emailReplyPayload.getReplySendTo().toArray(new String[0]));
        helper.setSubject(emailReplyPayload.getSubject());
        helper.setReplyTo(emailReplyPayload.getReply());
        helper.setFrom(emailReplyPayload.getEmailFrom());
        helper.setText(emailMessage, true); // 'true' to specify HTML content

        mailSender.send(mailMessage);
    }
}