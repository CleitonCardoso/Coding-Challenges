package com.n26.services;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.n26.dtos.Statistics;
import com.n26.dtos.Transaction;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {

	@InjectMocks
	private StatisticsService service;

	@Mock
	private TransactionsService transactionsService;

	@Test
	public void testGetTransactionsFromLast60Seconds() {
		Transaction transaction1 = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( new Date() )
				.build();

		Transaction transaction2 = Transaction.builder()
				.amount( BigDecimal.valueOf( 55 ) )
				.timestamp( new Date() )
				.build();

		List<Transaction> transactions = Arrays.asList( transaction1, transaction2 );
		Mockito.when( transactionsService.getTransactionsFromLast60Seconds() ).thenReturn( transactions );

		Statistics statisticsFromLast60Seconds = service.getStatisticsFromLast60Seconds();

		Mockito.verify( transactionsService ).getTransactionsFromLast60Seconds();
		Assert.assertThat( statisticsFromLast60Seconds.getAvg(), Matchers.notNullValue() );
		Assert.assertThat( statisticsFromLast60Seconds.getMax(), Matchers.notNullValue() );
		Assert.assertThat( statisticsFromLast60Seconds.getMin(), Matchers.notNullValue() );
		Assert.assertThat( statisticsFromLast60Seconds.getSum(), Matchers.notNullValue() );
		Assert.assertThat( statisticsFromLast60Seconds.getCount(), Matchers.notNullValue() );
	}

}
