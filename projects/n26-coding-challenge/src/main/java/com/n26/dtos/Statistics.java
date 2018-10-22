package com.n26.dtos;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Statistics {

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal sum;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal avg;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal max;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal min;

	private Long count;

	@SuppressWarnings("unused")
	private Statistics() {
	}

	public Statistics(List<Transaction> transactions) {
		this.count = Long.valueOf( transactions.size() );

		this.sum = transactions.stream()
				.map( Transaction::getAmount )
				.reduce( BigDecimal.ZERO, BigDecimal::add )
				.setScale( 2, BigDecimal.ROUND_HALF_UP );

		this.avg = this.sum.compareTo( BigDecimal.ZERO ) == 0 ? this.sum
				: this.sum.divide( BigDecimal.valueOf( count ), 2, BigDecimal.ROUND_HALF_UP );

		this.max = transactions.stream()
				.map( Transaction::getAmount )
				.max( Comparator.naturalOrder() )
				.orElse( BigDecimal.ZERO )
				.setScale( 2, BigDecimal.ROUND_HALF_UP );

		this.min = transactions.stream()
				.map( Transaction::getAmount )
				.min( Comparator.naturalOrder() )
				.orElse( BigDecimal.ZERO )
				.setScale( 2, BigDecimal.ROUND_HALF_UP );
	}

}
