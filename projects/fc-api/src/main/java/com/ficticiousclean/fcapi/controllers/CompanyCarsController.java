package com.ficticiousclean.fcapi.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ficticiousclean.fcapi.entities.CompanyCar;
import com.ficticiousclean.fcapi.services.CompanyCarsService;
import com.ficticiousclean.fcapi.vos.CompanyCarConsumptionVO;

@RestController
@RequestMapping("company-cars")
@Validated
public class CompanyCarsController {

	@Autowired
	private CompanyCarsService companyCarsService;

	@GetMapping("expected-consumption")
	public List<CompanyCarConsumptionVO> getCompanyCarsExpectedConsumption(
			final @RequestParam("gasoline-value-lt") BigDecimal gasolineValueLT,
			final @RequestParam("total-city-km") BigDecimal totalCityKM,
			final @RequestParam("total-highway-km") BigDecimal totalHighwayKM,
			final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize) {

		return companyCarsService.getCompanyCarsExpectedConsumption( gasolineValueLT, totalCityKM, totalHighwayKM, pageIndex, resultSize );
	}

	@PostMapping
	public CompanyCar create(final @RequestBody CompanyCar companyCar) {
		return companyCarsService.create( companyCar );
	}

	@DeleteMapping("/{uuid}")
	public void remove(final @PathVariable("uuid") UUID uuid) {
		companyCarsService.remove( uuid );
	}

	@PutMapping("/{uuid}")
	public CompanyCar update(final @PathVariable("uuid") UUID uuid, final @RequestBody CompanyCar companyCar) {
		return companyCarsService.update( uuid, companyCar );
	}

	@GetMapping
	public Page<CompanyCar> findAll(
			final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize) {
		return companyCarsService.findAll( pageIndex, resultSize );
	}

}
