package com.ficticiousclean.fcapi.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ficticiousclean.fcapi.repositories.CompanyCarsRepository;
import com.ficticiousclean.fcapi.vos.CompanyCarConsumptionVO;

@Service
public class CompanyCarsService {

	@Autowired
	private CompanyCarsRepository companyCarsRepository;

	public List<CompanyCarConsumptionVO> getCompanyCarsExpectedConsumption(BigDecimal gasolineValueLT, BigDecimal totalCityKM, BigDecimal totalHighwayKM, Integer pageIndex, Integer resultSize) {
		if (pageIndex == null || Integer.signum( pageIndex ) < 0) {
			pageIndex = 0;
		}
		if (resultSize == null || Integer.signum( resultSize ) < 0) {
			resultSize = 0;
		}
		PageRequest pageRequest = PageRequest.of( pageIndex, resultSize );
		return companyCarsRepository.getCompanyCarsExpectedConsumption( gasolineValueLT, totalCityKM, totalHighwayKM, pageRequest );
	}

}
