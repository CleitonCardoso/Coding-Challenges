package com.peixeurbano.SimpleDealApp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.peixeurbano.SimpleDealApp.models.Deal;

public interface DealRepository extends CrudRepository<Deal, Long> {

}
