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
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class EmailRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String emailFrom;
    
    @ElementCollection
    @CollectionTable(name="email_send_To", joinColumns = @JoinColumn(name="id"))
    @Column(name="sendTo")
    private List<String>  sendTo;
    
    @ElementCollection
    @CollectionTable(name="email_cc", joinColumns = @JoinColumn(name="id"))
    @Column(name="cc")
    private List<String> cc;
    
    @ElementCollection
    @CollectionTable(name="email_bcc", joinColumns = @JoinColumn(name="id"))
    @Column(name="bcc")
    private List<String> bcc;
    private String emailSubject;
    private String message;
    private String fileName;
    private Boolean status=false;
    
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateTime;
    private String recipientUsername;
   
   @OneToMany
   @JoinColumn(name="id")
   private List<EmailReply> emaleReply;
}