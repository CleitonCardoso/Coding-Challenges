package com.peixeurbano.SimpleDealApp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peixeurbano.SimpleDealApp.models.Deal;
import com.peixeurbano.SimpleDealApp.repositories.DealRepository;

@Service
public class DealService {

	@Autowired
	private DealRepository dealRepository;

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

}
