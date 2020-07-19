package com.ficticiousclean.fcapi.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ficticiousclean.fcapi.vos.ErrorMessageVO;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String name = ex.getParameterName();
		String message = "O parâmetro " + name + " é requerido.";
		return ResponseEntity.badRequest().body( new ErrorMessageVO( message, ex.getMessage() ) );
	}
}
