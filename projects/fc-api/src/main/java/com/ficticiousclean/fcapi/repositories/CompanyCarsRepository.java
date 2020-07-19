package com.ficticiousclean.fcapi.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.ficticiousclean.fcapi.entities.CompanyCar;
import com.ficticiousclean.fcapi.vos.CompanyCarConsumptionVO;

public interface CompanyCarsRepository extends JpaRepositoryImplementation<CompanyCar, UUID> {

	@Query("SELECT new com.ficticiousclean.fcapi.vos.CompanyCarConsumptionVO("
			+ " car.name,"
			+ " car.brand,"
			+ " car.model,"
			+ " year(car.manufacturingDate),"
			+ " (:totalCityKM / car.averageCityConsumption) + (:totalHighwayKM / car.averageHighwayConsumption),"
			+ " (((:totalCityKM / car.averageCityConsumption) + (:totalHighwayKM / car.averageHighwayConsumption)) * :gasolineValueLT ) "
			+ " )"
			+ " from CompanyCar car"
			+ " order by (((:totalCityKM / car.averageCityConsumption) + (:totalHighwayKM / car.averageHighwayConsumption)) * :gasolineValueLT ) ASC")
	public List<CompanyCarConsumptionVO> getCompanyCarsExpectedConsumption(@Param("gasolineValueLT") BigDecimal gasolineValueLT,
			@Param("totalCityKM") BigDecimal totalCityKM,
			@Param("totalHighwayKM") BigDecimal totalHighwayKM,
			Pageable pageRequest);

}
