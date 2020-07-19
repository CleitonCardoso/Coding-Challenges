package com.ficticiousclean.fcapi.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ficticiousclean.fcapi.services.CompanyCarsService;
import com.ficticiousclean.fcapi.vos.CompanyCarConsumptionVO;

@RestController
@RequestMapping("company-cars")
public class CompanyCarsController {

	@Autowired
	private CompanyCarsService companyCarsService;

	@GetMapping("expected-consumption")
	public List<CompanyCarConsumptionVO> getCompanyCarsExpectedConsumption(
			final @RequestParam("gasoline-value-lt") BigDecimal gasolineValueLT,
			final @RequestParam("total-city-km") BigDecimal totalCityKM,
			final @RequestParam("total-highway-km") BigDecimal totalHighwayKM,
			final @RequestParam("page-index") Integer pageIndex,
			final @RequestParam("result-size") Integer resultSize) {

		return companyCarsService.getCompanyCarsExpectedConsumption( gasolineValueLT, totalCityKM, totalHighwayKM, pageIndex, resultSize );
	}

}
