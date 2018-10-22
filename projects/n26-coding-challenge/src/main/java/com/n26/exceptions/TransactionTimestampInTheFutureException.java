package com.n26.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class TransactionTimestampInTheFutureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8640826385021615599L;

}
