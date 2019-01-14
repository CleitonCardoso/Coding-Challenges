package com.peixeurbano.SimpleDealApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peixeurbano.SimpleDealApp.models.BuyOption;
import com.peixeurbano.SimpleDealApp.repositories.BuyOptionRepository;

@Service
public class BuyOptionService {

	@Autowired
	private BuyOptionRepository buyOptionRepository;

	public BuyOption save(BuyOption buyOption) {
		return buyOptionRepository.save( buyOption );
	}

	public BuyOption findById(Long id) {
		return buyOptionRepository.findById( id ).orElse( null );
	}

}
