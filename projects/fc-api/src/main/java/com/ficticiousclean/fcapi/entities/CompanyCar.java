package com.ficticiousclean.fcapi.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "company_cars")
public class CompanyCar {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "uuid", updatable = false, nullable = false)
	@ColumnDefault("random_uuid()")
	@Type(type = "uuid-char")
	private UUID uuid;

	private String name;
	private String brand;
	private String model;
	private LocalDateTime manufacturingDate;
	private BigDecimal averageCityConsumption;
	private BigDecimal averageHighwayConsumption;

}
