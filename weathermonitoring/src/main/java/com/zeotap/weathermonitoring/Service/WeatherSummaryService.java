package com.zeotap.weathermonitoring.Service;

import com.zeotap.weathermonitoring.exception.ResourceNotFoundException;
import com.zeotap.weathermonitoring.Models.WeatherSummary;
import com.zeotap.weathermonitoring.Repository.WeatherSummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing weather summary data.
 */
@Service
public class WeatherSummaryService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherSummaryService.class);

    @Autowired
    private WeatherSummaryRepository weatherSummaryRepository;

    /**
     * Save or update the weather summary in the database.
     *
     * @param weatherSummary the weather summary to save
     * @throws ResourceNotFoundException if an error occurs while saving
     */
    public void saveWeatherSummary(WeatherSummary weatherSummary) {
        try {
            Optional<WeatherSummary> existingSummary = weatherSummaryRepository
                    .findByCityAndDate(weatherSummary.getCity(), weatherSummary.getDate());

            if (existingSummary.isPresent()) {
                WeatherSummary summaryToUpdate = existingSummary.get();
                summaryToUpdate.setAverageTemp(weatherSummary.getAverageTemp());
                summaryToUpdate.setMaxTemp(weatherSummary.getMaxTemp());
                summaryToUpdate.setMinTemp(weatherSummary.getMinTemp());
                summaryToUpdate.setDominantWeather(weatherSummary.getDominantWeather());
                 summaryToUpdate.setCurrentTemp(weatherSummary.getCurrentTemp()); // Set current temperature
                summaryToUpdate.setPressure(weatherSummary.getPressure()); // Set pressure
                summaryToUpdate.setHumidity(weatherSummary.getHumidity()); // Set humidity
                summaryToUpdate.setWindSpeed(weatherSummary.getWindSpeed());
                summaryToUpdate.setFeelsLike(weatherSummary.getFeelsLike());
                 // Set wind speed
                weatherSummaryRepository.save(summaryToUpdate);
                logger.info("Updated weather summary for {} on {}", weatherSummary.getCity(), weatherSummary.getDate());
            } else {
                weatherSummaryRepository.save(weatherSummary);
                logger.info("Saved new weather summary for {} on {}", weatherSummary.getCity(), weatherSummary.getDate());
            }
        } catch (Exception e) {
            logger.error("Error saving weather summary for {}: {}", weatherSummary.getCity(), e.getMessage());
            throw new ResourceNotFoundException("Failed to save weather summary", e);
        }
    }

    /**
     * Get all weather summaries for a specific city.
     *
     * @param city the name of the city
     * @return a list of weather summaries for the specified city
     */
    public List<WeatherSummary> getSummariesByCity(String city) {
        logger.info("Fetching weather summaries for city: {}", city);
        return weatherSummaryRepository.findAllByCity(city);
    }

    /**
     * Get a specific weather summary for a city and date.
     *
     * @param city the name of the city
     * @param date the date for the summary
     * @return an optional weather summary
     */
    public Optional<WeatherSummary> getSummaryByCityAndDate(String city, LocalDate date) {
        return weatherSummaryRepository.findByCityAndDate(city, date);
    }

    /**
     * Get all weather summaries.
     *
     * @return a list of all weather summaries
     */
    public List<WeatherSummary> getAllSummaries() {
        return weatherSummaryRepository.findAll();
    }

    
}
