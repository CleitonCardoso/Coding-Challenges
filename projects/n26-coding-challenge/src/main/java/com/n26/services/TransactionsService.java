package com.n26.services;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.n26.dtos.Transaction;
import com.n26.exceptions.TransactionTimestampInTheFutureException;
import com.n26.exceptions.TransactionTimestampTooOldException;

@Service
public class TransactionsService {

	private List<Transaction> transactions = new ArrayList<>();

	public List<Transaction> getTransactionsFromLast60Seconds() {
		return transactions.stream()
				.filter( t -> !isOlderThan60seconds( t ) )
				.collect( Collectors.toList() );
	}

	public void save(Transaction transaction)
			throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException {
		validateTransaction( transaction );
		clearTransactionsOlderThan60Seconds();
		transactions.add( transaction );
	}

	private void validateTransaction(Transaction transaction)
			throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException {
		if (isOlderThan60seconds( transaction ))
			throw new TransactionTimestampTooOldException();
		if (isInTheFuture( transaction )) {
			throw new TransactionTimestampInTheFutureException();
		}
	}

	private boolean isInTheFuture(Transaction transaction) {
		return transaction.getTimestamp().toInstant().compareTo( Instant.now() ) > 0;
	}

	private void clearTransactionsOlderThan60Seconds() {
		transactions = transactions.stream()
				.filter( t -> !isOlderThan60seconds( t ) )
				.collect( Collectors.toList() );
	}

	private boolean isOlderThan60seconds(Transaction transaction) {
		return Duration.between( transaction.getTimestamp().toInstant(), Instant.now() )
				.compareTo( Duration.ofSeconds( 60 ) ) > 0;
	}

	public void deleteAll() {
		transactions.removeAll( transactions );
	}

}
