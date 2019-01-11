package com.peixeurbano.SimpleDealApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.peixeurbano.SimpleDealApp.models.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {

}
