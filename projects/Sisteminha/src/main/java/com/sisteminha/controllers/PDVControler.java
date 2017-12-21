package com.sisteminha.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sisteminha.entities.PDV;
import com.sisteminha.repositories.PDVRepository;

@RequestMapping(path = "pdv")
@RestController
public class PDVControler {

	@Autowired
	private PDVRepository repository;

	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody PDV pdv) {
		repository.save(pdv);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody PDV pdv) {
		repository.save(pdv);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public PDV find(@PathVariable("id") Long id) {
		return repository.findOne(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<PDV> findAll() {
		return repository.findAll();
	}
}
