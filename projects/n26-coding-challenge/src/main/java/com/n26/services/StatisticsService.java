package com.n26.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.dtos.Statistics;

@Service
public class StatisticsService {

	@Autowired
	private TransactionsService transactionsService;

	public Statistics getStatisticsFromLast60Seconds() {
		return new Statistics( transactionsService.getTransactionsFromLast60Seconds() );
	}
}
