package com.peixeurbano.SimpleDealApp.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.peixeurbano.SimpleDealApp.models.BuyOption;
import com.peixeurbano.SimpleDealApp.repositories.BuyOptionRepository;

@RunWith(MockitoJUnitRunner.class)
public class BuyOptionServiceTest {

	@InjectMocks
	private BuyOptionService buyOptionService;

	@Mock
	private BuyOptionRepository buyOptionRepository;

	@Test
	public void findById() {
		Long buyOptionId = 1l;
		buyOptionService.findById( buyOptionId );
		Mockito.verify( buyOptionRepository ).findById( 1l );
	}

	@Test
	public void save() {
		BuyOption buyOption = new BuyOption();
		buyOptionService.save( buyOption );
		Mockito.verify( buyOptionRepository ).save( buyOption );
	}

}
