package com.dollop.app.payload;
import java.time.LocalDateTime;
import java.util.List;
import com.dollop.app.models.EmailRequest;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EmailReplyPlayload {
	   private Integer replyId;
	   private String emailFrom;
	   private List<String>  replySendTo;
	    private List<String> replyCc;
	    private List<String> replyBcc;
	    private String subject;
	    private String message;
	    private String fileName;
	    private Boolean status=false;
	    private LocalDateTime dateTime;
	    private String recipientUsername;
	    private String reply;
	    private EmailRequest emailRequest;
}
