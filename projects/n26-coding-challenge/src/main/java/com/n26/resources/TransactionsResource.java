package com.n26.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.dtos.Transaction;
import com.n26.exceptions.TransactionTimestampInTheFutureException;
import com.n26.exceptions.TransactionTimestampTooOldException;
import com.n26.services.TransactionsService;

@RestController
@RequestMapping(path = "transactions")
public class TransactionsResource {

	@Autowired
	private TransactionsService transactionsService;

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void saveTransaction(@Valid @RequestBody Transaction transaction) throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException {
		transactionsService.save( transaction );
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteAllTransactions() {
		transactionsService.deleteAll();
	}

}
