package com.peixeurbano.SimpleDealApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peixeurbano.SimpleDealApp.models.Deal;
import com.peixeurbano.SimpleDealApp.services.DealService;

@RestController
@RequestMapping("deal")
public class DealController {

	@Autowired
	private DealService dealService;

	@PostMapping
	public Deal save(Deal deal) {
		return dealService.save( deal );
	}

	@GetMapping
	public Deal find(Long id) {
		return dealService.findById( id );
	}

	@DeleteMapping
	public void delete(Long id) {
		dealService.delete( id );
	}

}
