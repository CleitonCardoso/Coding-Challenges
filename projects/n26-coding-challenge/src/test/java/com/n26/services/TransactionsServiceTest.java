package com.n26.services;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.n26.dtos.Transaction;
import com.n26.exceptions.TransactionTimestampInTheFutureException;
import com.n26.exceptions.TransactionTimestampTooOldException;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsServiceTest {

	@InjectMocks
	private TransactionsService service;

	@Test
	public void testSaveSuccess() throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException {
		Transaction transaction1 = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( new Date() )
				.build();

		service.save( transaction1 );
		List<Transaction> transactionsFromLast60Seconds = service.getTransactionsFromLast60Seconds();
		Assert.assertThat( transactionsFromLast60Seconds, Matchers.hasSize( 1 ) );
	}

	@Test(expected = TransactionTimestampTooOldException.class)
	public void testSaveTimestampTooOld()
			throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException {
		Calendar instantCalendar = Calendar.getInstance();
		instantCalendar.add( Calendar.MINUTE, -2 );
		Date timestampOlderThan60seconds = instantCalendar.getTime();
		Transaction transaction1 = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( timestampOlderThan60seconds )
				.build();

		service.save( transaction1 );
	}

	@Test(expected = TransactionTimestampInTheFutureException.class)
	public void testSaveTimestampInTheFuture()
			throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException {
		Calendar instantCalendar = Calendar.getInstance();
		instantCalendar.add( Calendar.DAY_OF_MONTH, 1 );
		Date timestampInTheFuture = instantCalendar.getTime();
		Transaction transaction1 = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( timestampInTheFuture )
				.build();

		service.save( transaction1 );
	}

	@Test
	public void testDeleteAll() throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException {
		Transaction transaction1 = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( new Date() )
				.build();

		service.save( transaction1 );
		service.deleteAll();
		List<Transaction> transactionsFromLast60Seconds = service.getTransactionsFromLast60Seconds();
		Assert.assertThat( transactionsFromLast60Seconds, Matchers.hasSize( 0 ) );
	}

	@Test
	public void clearOlderTransactionsWhileAddingNew()
			throws TransactionTimestampTooOldException, TransactionTimestampInTheFutureException, InterruptedException {
		Calendar instantCalendar = Calendar.getInstance();
		instantCalendar.add( Calendar.SECOND, -59 );
		Transaction almostOldTransaction = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( instantCalendar.getTime() )
				.build();

		service.save( almostOldTransaction );
		Thread.sleep( 1000l );

		Transaction newerTransaction = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( new Date() )
				.build();

		service.save( newerTransaction );

		List<Transaction> transactionsFromLast60Seconds = service.getTransactionsFromLast60Seconds();
		Assert.assertThat( transactionsFromLast60Seconds, Matchers.hasSize( 1 ) );
		Assert.assertThat( transactionsFromLast60Seconds.get( 0 ), Matchers.equalTo( newerTransaction ) );
	}
}
