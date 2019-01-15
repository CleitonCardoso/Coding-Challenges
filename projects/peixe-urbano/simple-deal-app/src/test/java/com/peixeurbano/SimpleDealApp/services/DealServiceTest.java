package com.peixeurbano.SimpleDealApp.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.peixeurbano.SimpleDealApp.exceptions.InvalidBuyOptionException;
import com.peixeurbano.SimpleDealApp.models.BuyOption;
import com.peixeurbano.SimpleDealApp.models.Deal;
import com.peixeurbano.SimpleDealApp.repositories.DealRepository;

@RunWith(MockitoJUnitRunner.class)
public class DealServiceTest {

	@InjectMocks
	private DealService dealService;

	@Mock
	private DealRepository dealRepository;

	@Mock
	private BuyOptionService buyOptionService;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	public void confirmSaleWithSuccess() throws InvalidBuyOptionException {
		LocalDate localDate = LocalDate.now().plus( Period.ofDays( 40 ) );
		Date endDate = Date.from( localDate.atStartOfDay( ZoneId.systemDefault() ).toInstant() );

		Long buyOptionId = 1l;

		Deal deal = Deal.builder()
				.endDate( endDate )
				.totalSold( 1l )
				.build();

		BuyOption buyOption = BuyOption.builder()
				.id( buyOptionId )
				.deal( deal )
				.startDate( new Date() )
				.endDate( endDate )
				.quantityCupom( 5l )
				.build();

		Mockito.when( buyOptionService.findById( buyOptionId ) ).thenReturn( buyOption );

		dealService.confirmSale( buyOptionId );

		Mockito.verify( buyOptionService ).save( buyOption );
		Mockito.verify( dealRepository ).save( deal );

		Assert.assertThat( deal.getTotalSold(), Matchers.equalTo( 2l ) );
		Assert.assertThat( buyOption.getQuantityCupom(), Matchers.equalTo( 4l ) );
	}

	@Test
	public void confirmSaleWithInvalidBuyOption() throws InvalidBuyOptionException {
		LocalDate localDate = LocalDate.now().plus( Period.ofDays( 40 ) );
		Date endDate = Date.from( localDate.atStartOfDay( ZoneId.systemDefault() ).toInstant() );

		Long buyOptionId = 1l;

		BuyOption buyOption = BuyOption.builder()
				.id( buyOptionId )
				.startDate( new Date() )
				.endDate( endDate )
				.quantityCupom( 5l )
				.build();

		Mockito.when( buyOptionService.findById( buyOptionId ) ).thenReturn( buyOption );

		exceptionRule.expect( InvalidBuyOptionException.class );
		exceptionRule.expectMessage( "A opção de compra selecionada é inválida." );

		dealService.confirmSale( buyOptionId );
	}

	@Test
	public void confirmSaleWithUnavailableDeal() throws InvalidBuyOptionException {
		LocalDate localDate = LocalDate.now().minus( Period.ofDays( 40 ) );
		Date endDate = Date.from( localDate.atStartOfDay( ZoneId.systemDefault() ).toInstant() );

		Long buyOptionId = 1l;

		Deal deal = Deal.builder()
				.endDate( endDate )
				.totalSold( 1l )
				.build();

		BuyOption buyOption = BuyOption.builder()
				.id( buyOptionId )
				.deal( deal )
				.startDate( new Date() )
				.endDate( endDate )
				.quantityCupom( 5l )
				.build();

		Mockito.when( buyOptionService.findById( buyOptionId ) ).thenReturn( buyOption );

		exceptionRule.expect( InvalidBuyOptionException.class );
		exceptionRule.expectMessage( "A opção de compra selecionada não está mais disponível." );

		dealService.confirmSale( buyOptionId );
	}
	
	@Test
	public void confirmSaleWithUnavailableCupoms() throws InvalidBuyOptionException {
		LocalDate localDate = LocalDate.now().plus( Period.ofDays( 40 ) );
		Date endDate = Date.from( localDate.atStartOfDay( ZoneId.systemDefault() ).toInstant() );

		Long buyOptionId = 1l;

		Deal deal = Deal.builder()
				.endDate( endDate )
				.totalSold( 1l )
				.build();

		BuyOption buyOption = BuyOption.builder()
				.id( buyOptionId )
				.deal( deal )
				.startDate( new Date() )
				.endDate( endDate )
				.quantityCupom( 0l )
				.build();

		Mockito.when( buyOptionService.findById( buyOptionId ) ).thenReturn( buyOption );

		exceptionRule.expect( InvalidBuyOptionException.class );
		exceptionRule.expectMessage( "A opção de compra selecionada não possui mais cupons ativos." );

		dealService.confirmSale( buyOptionId );
	}


	@Test
	public void delete() {
		Long dealId = 1l;
		dealService.delete( dealId );
		Mockito.verify( dealRepository ).deleteById( dealId );
	}

	@Test
	public void findById() {
		Long dealId = 1l;
		dealService.findById( dealId );
		Mockito.verify( dealRepository ).findById( dealId );
	}

	@Test
	public void save() {
		Deal deal = new Deal();
		dealService.save( deal );
		Mockito.verify( dealRepository ).save( deal );
	}

	@Test
	public void findAll() {
		dealService.findAll();
		Mockito.verify( dealRepository ).findAll();
	}

}
