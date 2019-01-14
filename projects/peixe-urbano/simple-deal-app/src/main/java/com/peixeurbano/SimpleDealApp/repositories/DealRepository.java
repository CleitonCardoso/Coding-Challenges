package com.peixeurbano.SimpleDealApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peixeurbano.SimpleDealApp.models.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {

}
