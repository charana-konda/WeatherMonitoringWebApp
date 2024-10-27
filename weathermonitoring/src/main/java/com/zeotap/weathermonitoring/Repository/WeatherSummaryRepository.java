package com.zeotap.weathermonitoring.Repository;

import com.zeotap.weathermonitoring.Models.WeatherSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing weather summary data from the database.
 */
public interface WeatherSummaryRepository extends JpaRepository<WeatherSummary, Long> {
    // Custom query to find weather summary by city and date
    Optional<WeatherSummary> findByCityAndDate(String city, LocalDate date);

    // Custom query to find all summaries for a specific city
    List<WeatherSummary> findAllByCity(String city);

   
}
