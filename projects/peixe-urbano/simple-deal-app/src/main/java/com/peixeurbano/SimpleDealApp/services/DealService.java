package com.peixeurbano.SimpleDealApp.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peixeurbano.SimpleDealApp.exceptions.InvalidBuyOptionException;
import com.peixeurbano.SimpleDealApp.models.BuyOption;
import com.peixeurbano.SimpleDealApp.models.Deal;
import com.peixeurbano.SimpleDealApp.repositories.DealRepository;

@Service
public class DealService {

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private BuyOptionService buyOptionService;

	public Deal save(Deal deal) {
		return dealRepository.save( deal );
	}

	public Deal findById(Long id) {
		return dealRepository.findById( id ).orElse( null );
	}

	public void delete(Long id) {
		dealRepository.deleteById( id );
	}

	public List<Deal> findAll() {
		return dealRepository.findAll();
	}

	public void confirmSale(Long buyOptionId) throws InvalidBuyOptionException {
		BuyOption buyOption = buyOptionService.findById( buyOptionId );
		validateBuyOptionSelected( buyOption );
		updateBuyOption( buyOption );
		updateDeal( buyOption.getDeal() );
	}

	private void updateBuyOption(BuyOption buyOption) {
		buyOption.setQuantityCupom( buyOption.getQuantityCupom() - 1 );
		buyOptionService.save( buyOption );
	}

	private void updateDeal(Deal deal) {
		deal.setTotalSold( deal.getTotalSold() + 1 );
		dealRepository.save( deal );
	}

	private void validateBuyOptionSelected(BuyOption buyOption) throws InvalidBuyOptionException {
		if (buyOption == null || buyOption.getDeal() == null)
			throw new InvalidBuyOptionException( "A opção de compra selecionada é inválida." );
		if (isDealDateEnded( buyOption ))
			throw new InvalidBuyOptionException( "A opção de compra selecionada não está mais disponível." );
		if (buyOption.getQuantityCupom() < 1)
			throw new InvalidBuyOptionException( "A opção de compra selecionada não possui mais cupons ativos." );
	}

	private boolean isDealDateEnded(BuyOption buyOption) {
		return LocalDate.now().atStartOfDay()
				.isAfter( buyOption.getDeal()
						.getEndDate().toInstant().atZone( ZoneId.systemDefault() )
						.toLocalDateTime() );
	}

}
