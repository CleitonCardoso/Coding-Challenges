package com.ficticiousclean.fcapi.vos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyCarConsumptionVO {

	private String name;
	private String brand;
	private String model;
	private Integer manufacturingYear;
	private BigDecimal totalFuelSpent;
	private BigDecimal totalFuelSupplyValue;
}
