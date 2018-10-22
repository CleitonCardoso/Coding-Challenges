package com.n26.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class TransactionTimestampTooOldException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8663482433902390753L;

}
