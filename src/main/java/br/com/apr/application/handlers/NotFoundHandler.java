package br.com.apr.application.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.apr.domain.exceptions.FornecedorNotFoundException;
import br.com.apr.domain.exceptions.ProdutoNotFoundException;

@ControllerAdvice
public class NotFoundHandler {
	
	@ExceptionHandler(ProdutoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public List<String> errorHandler(ProdutoNotFoundException e) {
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}
	
	@ExceptionHandler(FornecedorNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public List<String> errorHandler(FornecedorNotFoundException e) {
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}
}
