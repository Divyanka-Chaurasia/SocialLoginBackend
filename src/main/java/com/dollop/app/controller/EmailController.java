package com.dollop.app.controller;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.dollop.app.models.EmailRequest;
import com.dollop.app.payload.ApiResponse;
import com.dollop.app.payload.EmailRequestPayload;
import com.dollop.app.service.IFileService;
import com.dollop.app.service.ImageService;
import com.dollop.app.service.SendEmailService;
import com.dollop.app.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin("*")
public class EmailController {
	@Autowired
	private SendEmailService emailService;
	
	private EmailRequestPayload readValue;
	
	@Autowired
	private IFileService fileService;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${user.profile.file.path}")
	private String fileUploadPath;
	
	@PostMapping("/send")
	public ResponseEntity<?> sendEmail(@RequestBody EmailRequestPayload emailRequestPayload) throws MessagingException
	{
	   emailService.sendEmailWithHtml(emailRequestPayload);
	   return ResponseEntity.ok(
		ApiResponse.builder().message("Email send successfully !!").httpStatus(HttpStatus.OK).success(true).build());
	}
	 @PostMapping("/sendWithFile")
	  public ResponseEntity<ApiResponse> sendWithFile(@RequestPart("emailRequest") String emailRequest, @RequestPart("fileName") MultipartFile file) throws IOException, MessagingException {
	    ObjectMapper mapper = new ObjectMapper();
	     readValue = mapper.readValue(emailRequest, EmailRequestPayload.class);
//	     String imageName = fileService.uploadFile(file, fileUploadPath);
	     String imageName = imageService.uploadImage(file, AppConstants.FILES);
	     readValue.setFileName(imageName);
	    emailService.sendEmailWithFile(readValue,file);
	    return ResponseEntity.ok(
	      ApiResponse.builder().message("Email sent successfully !!").httpStatus(HttpStatus.OK).success(true).build()
	    );
	  }
	@GetMapping("/findAllInboxMails")
	public ResponseEntity<List<EmailRequest>> findAllInbox()
	{
		return new ResponseEntity<>(emailService.findAllInboxEmail(),HttpStatus.OK);
	}
	@GetMapping("/findAllSentMails")
	public ResponseEntity<List<EmailRequest>> findAllSent()
	{
		return new ResponseEntity<>(emailService.findAllSentEmail(),HttpStatus.OK);
	}
	@GetMapping("/updateStatus/{id}/{favorite}")
	public ResponseEntity<Map<String,Object>> updateStatus(@PathVariable Integer id,@PathVariable Boolean favorite)
	{
		return new ResponseEntity<>(emailService.updateFavorite(id,favorite),HttpStatus.OK);
	}
	@GetMapping("/getFavorite")
	public ResponseEntity<List<EmailRequestPayload>> getFavoriteEmails()
	{
		return new ResponseEntity<>(emailService.findALlEmailByFavorite(),HttpStatus.OK);
	}	
	@GetMapping("/getEmail/{id}")
	public ResponseEntity<EmailRequestPayload> getEmailById(@PathVariable Integer id)
	{
		return new ResponseEntity<>(emailService.getEmail(id),HttpStatus.OK);
	}
	
	@GetMapping("/search/{email}/{loginUser}")
	public ResponseEntity<?> searchByEmail(@PathVariable String email,@PathVariable String loginUser)
	{
		return new ResponseEntity<>(emailService.searchByEmailFields(email,loginUser),HttpStatus.OK);
	}
	
	@GetMapping("/image/{prodId}")
	public void serveProdImage(@PathVariable Integer id,HttpServletResponse response) throws IOException
	{
		EmailRequestPayload emailRequestPayload = emailService.getEmail(id);
		InputStream resource = fileService.getResource(fileUploadPath, emailRequestPayload.getFileName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
	@GetMapping("/FindAllStarStatus")
	public ResponseEntity<?> findAllStatus()
	{
		return new ResponseEntity<>(emailService.findAllEmailStatus(),HttpStatus.OK);
	}
}