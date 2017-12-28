package com.sisteminha.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sisteminha.entities.PDV;
import com.sisteminha.repositories.PDVRepository;

@Service
public class PDVService {

	@Autowired
	private PDVRepository pdvRepository;

	public PDV find(Long id) {
		return pdvRepository.findOne( id );
	}

	public void delete(Long id) {
		pdvRepository.delete( id );
	}

	public PDV save(PDV pdv) {
		return pdvRepository.save( pdv );
	}

	public Iterable<PDV> list() {
		return pdvRepository.findAll();
	}

}
