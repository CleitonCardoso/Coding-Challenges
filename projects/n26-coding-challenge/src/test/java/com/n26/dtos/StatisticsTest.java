package com.n26.dtos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class StatisticsTest {

	private static final BigDecimal ROUNDED_HALF_UP_ZERO = BigDecimal.ZERO.setScale( 2, BigDecimal.ROUND_HALF_UP );

	@Test
	public void testEmptyStatistics() {
		Statistics emptyStatistics = new Statistics( Collections.emptyList() );
		Assert.assertThat( emptyStatistics.getAvg(), Matchers.equalTo( ROUNDED_HALF_UP_ZERO ) );
		Assert.assertThat( emptyStatistics.getMax(), Matchers.equalTo( ROUNDED_HALF_UP_ZERO ) );
		Assert.assertThat( emptyStatistics.getMin(), Matchers.equalTo( ROUNDED_HALF_UP_ZERO ) );
		Assert.assertThat( emptyStatistics.getSum(), Matchers.equalTo( ROUNDED_HALF_UP_ZERO ) );
		Assert.assertThat( emptyStatistics.getCount(), Matchers.equalTo( 0l ) );
	}

	@Test
	public void testStatisticsForSingleTransaction() {
		BigDecimal amountValue = BigDecimal.valueOf( 45.25 );
		Transaction transaction = Transaction.builder()
				.amount( amountValue )
				.timestamp( new Date() )
				.build();
		List<Transaction> singleTransactionList = Arrays.asList( transaction );
		Statistics statistics = new Statistics( singleTransactionList );
		Assert.assertThat( statistics.getAvg(), Matchers.equalTo( amountValue ) );
		Assert.assertThat( statistics.getMax(), Matchers.equalTo( amountValue ) );
		Assert.assertThat( statistics.getMin(), Matchers.equalTo( amountValue ) );
		Assert.assertThat( statistics.getSum(), Matchers.equalTo( amountValue ) );
		Assert.assertThat( statistics.getCount(), Matchers.equalTo( 1l ) );
	}

	@Test
	public void testStatisticsForMultipleTransactions() {
		Transaction maximumAmountValueTransaction = Transaction.builder()
				.amount( BigDecimal.valueOf( 1000.56 ) )
				.timestamp( new Date() )
				.build();

		Transaction minimumAmountValueTransaction = Transaction.builder()
				.amount( BigDecimal.valueOf( 10.50 ) )
				.timestamp( new Date() )
				.build();

		Transaction midTransaction1 = Transaction.builder()
				.amount( BigDecimal.valueOf( 50 ) )
				.timestamp( new Date() )
				.build();

		Transaction midTransaction2 = Transaction.builder()
				.amount( BigDecimal.valueOf( 55 ) )
				.timestamp( new Date() )
				.build();

		List<Transaction> transactions = Arrays.asList( minimumAmountValueTransaction,
				maximumAmountValueTransaction,
				midTransaction1,
				midTransaction2 );

		Statistics statistics = new Statistics( transactions );
		Assert.assertThat( statistics.getAvg(),
				Matchers.equalTo( BigDecimal.valueOf( 279.015 )
						.setScale( 2, BigDecimal.ROUND_HALF_UP ) ) );
		Assert.assertThat( statistics.getMax(),
				Matchers.equalTo( maximumAmountValueTransaction.getAmount()
						.setScale( 2, BigDecimal.ROUND_HALF_UP ) ) );
		Assert.assertThat( statistics.getMin(),
				Matchers.equalTo( minimumAmountValueTransaction.getAmount()
						.setScale( 2, BigDecimal.ROUND_HALF_UP ) ) );
		Assert.assertThat( statistics.getSum(), Matchers.equalTo( BigDecimal.valueOf( 1116.06 ) ) );
		Assert.assertThat( statistics.getCount(), Matchers.equalTo( 4l ) );
	}

}
