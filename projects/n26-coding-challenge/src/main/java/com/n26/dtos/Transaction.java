package com.n26.dtos;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {

	@NotNull
	private BigDecimal amount;
	@NotNull
	private Date timestamp;
	
}
