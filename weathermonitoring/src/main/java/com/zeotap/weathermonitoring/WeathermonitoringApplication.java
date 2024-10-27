package com.zeotap.weathermonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeathermonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeathermonitoringApplication.class, args);
	}

}
