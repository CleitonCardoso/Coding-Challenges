package com.ficticiousclean.fcapi.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ficticiousclean.fcapi.entities.CompanyCar;
import com.ficticiousclean.fcapi.repositories.CompanyCarsRepository;
import com.ficticiousclean.fcapi.vos.CompanyCarConsumptionVO;

@Service
public class CompanyCarsService {

	@Autowired
	private CompanyCarsRepository companyCarsRepository;

	public List<CompanyCarConsumptionVO> getCompanyCarsExpectedConsumption(BigDecimal gasolineValueLT, BigDecimal totalCityKM, BigDecimal totalHighwayKM, Integer pageIndex, Integer resultSize) {
		PageRequest pageRequest = PageRequest.of( pageIndex, resultSize );
		return companyCarsRepository.getCompanyCarsExpectedConsumption( gasolineValueLT, totalCityKM, totalHighwayKM, pageRequest );
	}

	public CompanyCar create(CompanyCar companyCar) {
		return companyCarsRepository.save( companyCar );
	}

	public void remove(UUID uuid) {
		companyCarsRepository.deleteById( uuid );
	}

	public CompanyCar update(UUID uuid, CompanyCar companyCar) {
		companyCar.setUuid( uuid );
		return companyCarsRepository.save( companyCar );
	}

	public Page<CompanyCar> findAll(Integer pageIndex, Integer resultSize) {
		PageRequest pageRequest = PageRequest.of( pageIndex, resultSize );
		return companyCarsRepository.findAll( pageRequest );
	}

}
