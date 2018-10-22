package com.n26.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.dtos.Statistics;
import com.n26.services.StatisticsService;

@RestController
@RequestMapping("statistics")
public class StatisticsResource {

	@Autowired
	private StatisticsService statisticsService;

	@RequestMapping(method = RequestMethod.GET)
	public Statistics getStatisticsFromLast60Seconts() {
		return statisticsService.getStatisticsFromLast60Seconds();
	}

}
