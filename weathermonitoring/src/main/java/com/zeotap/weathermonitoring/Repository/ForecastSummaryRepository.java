package com.zeotap.weathermonitoring.Repository;


import com.zeotap.weathermonitoring.Models.ForecastSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ForecastSummaryRepository extends JpaRepository<ForecastSummary, Long> {
   
    List<ForecastSummary> findByCity(String city);
    ForecastSummary findByCityAndDate(String city, LocalDate date); // Method to find forecast by city and date
}
