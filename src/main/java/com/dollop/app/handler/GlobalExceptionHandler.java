package com.dollop.app.handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dollop.app.exception.BadApiRequestException;
import com.dollop.app.exception.ResourcesNotFoundException;
import com.dollop.app.payload.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(ResourcesNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundException(ResourcesNotFoundException e)
	{
		logger.info("exception hanlder is line");
		ApiResponse response = ApiResponse.builder() 
				.message(e.getMessage()) 
				.httpStatus(HttpStatus.NOT_FOUND) 
				.success(false).build();
		
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponse> badResponseException(BadApiRequestException b)
	{
		logger.info("badResponseException hanlder is line");
		ApiResponse response = ApiResponse.builder() 
				.message(b.getMessage()) 
				.httpStatus(HttpStatus.BAD_REQUEST) 
				.success(true).build();
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
}
