package com.zeotap.weathermonitoring.Controller;


import java.util.List;


import com.zeotap.weathermonitoring.Service.WeatherScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zeotap.weathermonitoring.Models.Alert;
import com.zeotap.weathermonitoring.Models.ForecastSummary;
import com.zeotap.weathermonitoring.Models.WeatherSummary;
import com.zeotap.weathermonitoring.Repository.AlertRepository;
import com.zeotap.weathermonitoring.Repository.ForecastSummaryRepository;
import com.zeotap.weathermonitoring.Service.WeatherSummaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for handling weather-related requests.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherSummaryService weatherSummaryService;

   
    @Autowired
    private WeatherScheduler weatherScheduler;


      @Autowired
    private AlertRepository alertRepository;


   @Autowired
    private ForecastSummaryRepository forecastSummaryRepository; 
    /**
     * Endpoint to get weather summaries for a specific city.
     *
     * @param city the name of the city
     * @return list of weather summaries for the specified city
     */
    @GetMapping("/summaries/{city}")
    public ResponseEntity<List<WeatherSummary>> getWeatherSummaries(@PathVariable String city) {
        logger.info("Received request for weather summaries for city: {}", city);
        List<WeatherSummary> summaries = weatherSummaryService.getSummariesByCity(city);

        if (summaries.isEmpty()) {
            logger.warn("No weather summaries found for city: {}", city);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("Retrieved {} summaries for city: {}", summaries.size(), city);
        return ResponseEntity.ok(summaries);
    }

    @PostMapping("/fetchWeatherData")
    public ResponseEntity<String> triggerFetchWeatherData() {
        weatherScheduler.fetchWeatherData(); // This calls the method directly
        return ResponseEntity.ok("Triggered weather data fetch.");
    }


    /**
     * Endpoint to set an alert for a specific city and email.
     *
     * @param city      the name of the city
     * @param email     the email to receive the alert
     * @param threshold the temperature threshold
     * @return response indicating if an alert email was sent
     */
    @PostMapping("/set")
    public ResponseEntity<String> setAlert(
            @RequestParam String city,
            @RequestParam String email,
            @RequestParam double threshold) {

        logger.info("Setting alert for city: {}, email: {}, threshold: {}", city, email, threshold);

        Alert alert = new Alert();
        alert.setCity(city);
        alert.setEmail(email);
        alert.setThreshold(threshold);
        alertRepository.save(alert);

        return ResponseEntity.ok("Alert set successfully.");
    }

     /**
     * Endpoint to get the 5-day weather forecast for a specific city from the database.
     *
     * @param city the name of the city
     * @return list of forecast summaries for the specified city
     */
    @GetMapping("/forecast/{city}")
    public ResponseEntity<List<ForecastSummary>> get5DayForecast(@PathVariable String city) {
        logger.info("Received request for 5-day forecast for city: {}", city);
        
        // Fetch forecast summaries from the database
        List<ForecastSummary> forecasts = forecastSummaryRepository.findByCity(city);

        if (!forecasts.isEmpty()) {
            logger.info("Retrieved {} forecast summaries for city: {}", forecasts.size(), city);
            return ResponseEntity.ok(forecasts); // Return the forecast summaries if found
        } else {
            logger.warn("No forecast data found for city: {}", city);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
    
