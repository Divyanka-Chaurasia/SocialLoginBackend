package com.dollop.app.models;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmailReply {

	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   @Id
	   private Integer replyId;
	   private String emailFrom;
	    
	    @ElementCollection
	    @CollectionTable(name="reply_send_To", joinColumns = @JoinColumn(name="id"))
	    @Column(name="replySendTo")
	    private List<String>  replySendTo;
	    
	    @ElementCollection
	    @CollectionTable(name="reply_email_cc", joinColumns = @JoinColumn(name="id"))
	    @Column(name="replyCc")
	    private List<String> replyCc;
	    
	    @ElementCollection
	    @CollectionTable(name="reply_email_bcc", joinColumns = @JoinColumn(name="id"))
	    @Column(name="replyBcc")
	    private List<String> replyBcc;
	    
	    private String subject;
	    private String message;
	    private String fileName;
	    private Boolean status=false;
	    
	    @JsonSerialize(using = LocalDateTimeSerializer.class)
		@JsonDeserialize(using = LocalDateTimeDeserializer.class)
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	    private LocalDateTime dateTime;
	    private String recipientUsername;
	    
	    private String reply;
	    
		@ManyToOne
		@JoinColumn(name="id")
		private EmailRequest emailRequest;
}
