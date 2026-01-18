package com.ey.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice//get executed at the beginning of spring app
public class CustomExceptionHandler extends Exception{
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleNotValidException(MethodArgumentNotValidException ex){
		
		Map<String,List<String>> body=new HashMap<>();
		
		List<String> errors=ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.toList());
		
		body.put("errors", errors);
		
		return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
		
	}
	
	
	@ExceptionHandler(UserOperationException.class)
	public ResponseEntity<Object> handleNotValidException(UserOperationException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(CustomerOperationException.class)
	public ResponseEntity<Object> handleNotValidException(CustomerOperationException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(DealerOperationException.class)
	public ResponseEntity<Object> handleNotValidException(DealerOperationException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(MedicineOperationException.class)
	public ResponseEntity<Object> handleNotValidException(MedicineOperationException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(SaleOperationException.class)
	public ResponseEntity<Object> handleNotValidException(SaleOperationException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(WalletNotFoundException.class)
	public ResponseEntity<Object> handleNotValidException(WalletNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(OfferNotFoundException.class)
	public ResponseEntity<Object> handleNotValidException(OfferNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(ExpenseNotFoundException.class)
	public ResponseEntity<Object> handleNotValidException(ExpenseNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(AlertNotFoundException.class)
	public ResponseEntity<Object> handleNotValidException(AlertNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(RefillNotFoundException.class)
	public ResponseEntity<Object> handleNotValidException(RefillNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
	
}


