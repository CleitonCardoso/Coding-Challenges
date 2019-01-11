package com.peixeurbano.SimpleDealApp.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peixeurbano.SimpleDealApp.models.Deal;
import com.peixeurbano.SimpleDealApp.services.DealService;

@CrossOrigin
@RestController
@RequestMapping("deal")
public class DealController {

	@Autowired
	private DealService dealService;

	@PostMapping
	public Deal save(@RequestBody Deal deal) {
		return dealService.save( deal );
	}
	
	@GetMapping
	public List<Deal> findAll(){
		return dealService.findAll();
	}

	@GetMapping("{id}")
	public Deal find(@PathParam("id") Long id) {
		return dealService.findById( id );
	}

	@DeleteMapping("{id}")
	public void delete(@PathParam("id") Long id) {
		dealService.delete( id );
	}

}
