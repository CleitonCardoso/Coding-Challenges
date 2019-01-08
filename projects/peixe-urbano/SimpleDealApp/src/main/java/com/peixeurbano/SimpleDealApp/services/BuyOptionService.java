package com.peixeurbano.SimpleDealApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peixeurbano.SimpleDealApp.models.BuyOption;

@Service
public class BuyOptionService {

	@Autowired
	private BuyOptionService buyOptionService;

	public BuyOption save(BuyOption buyOption) {
		return buyOptionService.save( buyOption );
	}

	public BuyOption findById(Long id) {
		return buyOptionService.findById( id );
	}

}
