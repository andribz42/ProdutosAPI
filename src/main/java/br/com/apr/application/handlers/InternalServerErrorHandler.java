package br.com.apr.application.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InternalServerErrorHandler {
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public List<String> errorHandler(IllegalArgumentException e) {
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public List<String> errorHandler(DataIntegrityViolationException e) {
		List<String> errors = new ArrayList<String>();
	
		String message;
		
		if(e.getMessage().contains("in column")) {
			String column = e.getMessage().substring(
					e.getMessage().indexOf("\"") + 1, 
					e.getMessage().indexOf("\" violates"));
			message = "O campo '" + column + "' não foi preenchido corretamente.";
		} else if(e.getMessage().contains("violates unique constraint")) {
			message = "O registro já foi cadastrado.";
		}else {
			message = e.getMostSpecificCause().getMessage();
		}
		
		errors.add(message);
		return errors;
	}
}

