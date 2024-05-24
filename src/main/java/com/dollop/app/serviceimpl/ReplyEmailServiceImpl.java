package com.dollop.app.serviceimpl;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dollop.app.models.EmailReply;
import com.dollop.app.payload.EmailReplyPlayload;
import com.dollop.app.repositories.EmailReplyRepo;
import com.dollop.app.service.ReplyEmailService;
import com.dollop.app.util.EmailUtil;
import jakarta.mail.MessagingException;

@Service
public class ReplyEmailServiceImpl implements ReplyEmailService {

	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private EmailReplyRepo emailReplyRepo;
	
	@Override
	public Map<String,Object> emailReply(EmailReplyPlayload emailReplyPlayload) throws MessagingException {
		 emailReplyPlayload.setDateTime(LocalDateTime.now());
		 EmailReply emailReply = this.emailReplyPayloadToEmailReply(emailReplyPlayload);
		 emailUtil.sendReplyEmailWithHtml(emailReplyPlayload);
		 emailReplyRepo.save(emailReply);	
		 Map<String,Object> map = new HashMap<>();
		 map.put("msg", "reply sent successfully");
		 return map;
	}

	public EmailReply emailReplyPayloadToEmailReply(EmailReplyPlayload emailReplyPlayload)
	{
		EmailReply emailReply = new EmailReply();
		emailReply.setReplyBcc(emailReplyPlayload.getReplyBcc());
		emailReply.setReplyCc(emailReplyPlayload.getReplyCc());
		emailReply.setDateTime(emailReplyPlayload.getDateTime());
		emailReply.setStatus(emailReplyPlayload.getStatus());
		emailReply.setFileName(emailReplyPlayload.getFileName());
		emailReply.setReplyId(emailReplyPlayload.getReplyId());
		emailReply.setMessage(emailReplyPlayload.getMessage());
		emailReply.setRecipientUsername(emailReplyPlayload.getRecipientUsername());
		emailReply.setReplySendTo(emailReplyPlayload.getReplySendTo());
		emailReply.setEmailFrom(emailReplyPlayload.getEmailFrom());
		return emailReply;
	}
	public EmailReplyPlayload emailRequestPayloadToEmailReply(EmailReply emailReply)
	{
		EmailReplyPlayload emailRequestPayload = new EmailReplyPlayload();
		emailRequestPayload.setReplyBcc(emailReply.getReplyBcc());
		emailRequestPayload.setReplyCc(emailReply.getReplyCc());
		emailRequestPayload.setDateTime(emailReply.getDateTime());
		emailRequestPayload.setStatus(emailReply.getStatus());
		emailRequestPayload.setFileName(emailReply.getFileName());
		emailRequestPayload.setReplyId(emailReply.getReplyId());
		emailRequestPayload.setMessage(emailReply.getMessage());
		emailRequestPayload.setRecipientUsername(emailReply.getRecipientUsername());
		emailRequestPayload.setReplySendTo(emailReply.getReplySendTo());
		emailRequestPayload.setSubject(emailReply.getSubject());
		return emailRequestPayload;
	}
	
}
