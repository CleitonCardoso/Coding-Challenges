package com.n26.dtos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransactionTest {

	private static Validator validator;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void transactionSuccessValidation() {
		Transaction transaction = Transaction.builder()
				.amount( BigDecimal.valueOf( 45.25 ) )
				.timestamp( new Date() )
				.build();
		Set<ConstraintViolation<Transaction>> violations = validator.validate( transaction );
		Assert.assertTrue( violations.isEmpty() );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void transactionFailedValidationOnTimestamp() {
		Transaction transaction = Transaction.builder()
				.amount( BigDecimal.valueOf( 45.25 ) )
				.timestamp( null )
				.build();
		Set<ConstraintViolation<Transaction>> violations = validator.validate( transaction );
		Assert.assertFalse( violations.isEmpty() );
		Assert.assertThat( violations.size(), Matchers.equalTo( 1 ) );

		ConstraintViolation<Transaction> violation = (ConstraintViolation<Transaction>) violations.toArray()[0];
		Assert.assertThat( violation.getPropertyPath().toString(), Matchers.equalTo( "timestamp" ) );
	}

	@Test
	@SuppressWarnings("unchecked")
	public void transactionFailedValidationOnAmount() {
		Transaction transaction = Transaction.builder()
				.amount( null )
				.timestamp( new Date() )
				.build();
		Set<ConstraintViolation<Transaction>> violations = validator.validate( transaction );
		Assert.assertFalse( violations.isEmpty() );
		Assert.assertThat( violations.size(), Matchers.equalTo( 1 ) );

		ConstraintViolation<Transaction> violation = (ConstraintViolation<Transaction>) violations.toArray()[0];
		Assert.assertThat( violation.getPropertyPath().toString(), Matchers.equalTo( "amount" ) );
	}

}
