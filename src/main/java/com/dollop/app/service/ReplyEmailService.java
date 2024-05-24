package com.dollop.app.service;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.dollop.app.payload.EmailReplyPlayload;

import jakarta.mail.MessagingException;

public interface ReplyEmailService {

	Map<String,Object> emailReply(EmailReplyPlayload emailReplyPlayload) throws MessagingException;

}
