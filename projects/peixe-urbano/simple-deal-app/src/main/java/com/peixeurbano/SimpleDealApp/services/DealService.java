package com.peixeurbano.SimpleDealApp.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peixeurbano.SimpleDealApp.exceptions.InvalidBuyOption;
import com.peixeurbano.SimpleDealApp.models.BuyOption;
import com.peixeurbano.SimpleDealApp.models.Deal;
import com.peixeurbano.SimpleDealApp.repositories.BuyOptionRepository;
import com.peixeurbano.SimpleDealApp.repositories.DealRepository;

@Service
public class DealService {

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private BuyOptionRepository buyOptionRepository;

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

	public void confirmSale(Long buyOptionId) throws InvalidBuyOption {
		BuyOption buyOption = buyOptionRepository.findById( buyOptionId ).orElse( null );
		validateBuyOptionSelected( buyOption );
		updateBuyOption( buyOption );
		updateDeal( buyOption.getDeal() );
	}

	private void updateBuyOption(BuyOption buyOption) {
		buyOption.setQuantityCupom( buyOption.getQuantityCupom() - 1 );
		buyOptionRepository.save( buyOption );
	}

	private void updateDeal(Deal deal) {
		deal.setTotalSold( deal.getTotalSold() + 1 );
		dealRepository.save( deal );
	}

	private void validateBuyOptionSelected(BuyOption buyOption) throws InvalidBuyOption {
		if (buyOption == null || buyOption.getDeal() == null)
			throw new InvalidBuyOption( "A opção de compra selecionada é inválida." );
		if (isDealDateEnded( buyOption ))
			throw new InvalidBuyOption( "A opção de compra selecionada não está mais disponível." );
		if (buyOption.getQuantityCupom() < 1)
			throw new InvalidBuyOption( "A opção de compra selecionada não possui mais cupons ativos." );
	}

	private boolean isDealDateEnded(BuyOption buyOption) {
		return LocalDate.now().atStartOfDay()
				.isBefore( buyOption.getDeal()
						.getEndDate().toInstant().atZone( ZoneId.systemDefault() )
						.toLocalDateTime() );
	}

}
