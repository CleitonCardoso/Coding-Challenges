package com.sisteminha.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sisteminha.entities.PDV;
import com.sisteminha.services.PDVService;

@RequestMapping(path = "pdv")
@RestController
public class PDVController {

	@Autowired
	private PDVService pdvService;

	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody PDV pdv) {
		pdvService.save(pdv);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody PDV pdv) {
		pdvService.save(pdv);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public PDV find(@PathVariable("id") Long id) {
		return pdvService.find(id);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		pdvService.delete(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<PDV> findAll() {
		return pdvService.list();
	}
}
