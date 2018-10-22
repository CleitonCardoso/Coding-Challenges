package com.n26;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String... args) {
		SpringApplication.run( Application.class, args );
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault( TimeZone.getTimeZone( "UTC" ) );
	}

}
