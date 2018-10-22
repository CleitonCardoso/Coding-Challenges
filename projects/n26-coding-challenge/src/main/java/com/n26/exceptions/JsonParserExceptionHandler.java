package com.n26.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class JsonParserExceptionHandler {

	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(InvalidFormatException.class)
	public void handleConflict() {
		// Nothing to do
	}

}
