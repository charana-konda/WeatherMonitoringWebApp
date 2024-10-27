package com.zeotap.weathermonitoring.Service;

import com.zeotap.weathermonitoring.Models.Alert; // Import Alert model
import com.zeotap.weathermonitoring.Models.WeatherResponse;
import com.zeotap.weathermonitoring.Models.WeatherSummary;
import com.zeotap.weathermonitoring.Repository.AlertRepository; // Import Alert repository
import com.zeotap.weathermonitoring.Repository.WeatherSummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for scheduling weather data fetching from the OpenWeatherMap API.
 */
@Service
public class WeatherScheduler {
    private static final Logger logger = LoggerFactory.getLogger(WeatherScheduler.class);

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherSummaryRepository weatherSummaryRepository;

    @Autowired
    private AlertRepository alertRepository; // Inject AlertRepository

    @Autowired
    private EmailAlertService emailAlertService; // Inject EmailAlertService

    /**
     * Scheduled method to fetch weather data every 5 minutes.
     */
    @Scheduled(fixedRate = 300000)  // Every 5 minutes
    public void fetchWeatherData() {
        String[] cities = {
                "Delhi", "Jaipur", "Agra", "Varanasi", "Dehradun", "Chandigarh",
                "Mumbai", "Pune", "Ahmedabad", "Surat", "Nagpur", "Bangalore",
                "Chennai", "Hyderabad", "Coimbatore", "Thiruvananthapuram",
                "Kolkata", "Bhubaneswar", "Guwahati", "Patna", "Durgapur",
                "Bhopal", "Indore", "Shillong", "Imphal", "Agartala",
                "Aizawl", "Srinagar", "Leh", "Puducherry", "Mysuru",
                "Vadodara", "Jodhpur", "Nashik"
        };

        for (String city : cities) {
            WeatherResponse weatherResponse = weatherService.getWeather(city);
            if (weatherResponse != null && weatherResponse.getMain() != null) {
                double tempCelsius = weatherService.convertKelvinToCelsius(weatherResponse.getMain().getTemp());
                double feelsLike = weatherService.convertKelvinToCelsius(weatherResponse.getMain().getFeels_like());
                double pressure = weatherResponse.getMain().getPressure();
                int humidity = weatherResponse.getMain().getHumidity();
                double windSpeed = weatherResponse.getWind().getSpeed();

                // Get longitude and latitude from the weather response
                double lon = weatherResponse.getCoord().getLon();
                double lat = weatherResponse.getCoord().getLat();

                // Create or update daily weather summary
                LocalDate today = LocalDate.now();
                Optional<WeatherSummary> optionalSummary = weatherSummaryRepository.findByCityAndDate(city, today);

                if (optionalSummary.isEmpty()) {
                    // No existing summary for today, create a new one
                    WeatherSummary summary = new WeatherSummary();
                    summary.setCity(city);
                    summary.setCurrentTemp(tempCelsius);
                    summary.setFeelsLike(feelsLike); // Set current temperature
                    summary.setPressure((int) pressure); // Set pressure
                    summary.setHumidity(humidity); // Set humidity
                    summary.setWindSpeed(windSpeed); // Set wind speed
                    summary.setAverageTemp(tempCelsius);
                    summary.setMaxTemp(tempCelsius);
                    summary.setMinTemp(tempCelsius);
                    summary.setDominantWeather(weatherResponse.getWeather().get(0).getMain());
                    summary.setDate(today);
                    summary.setLon(lon); // Set longitude
                    summary.setLat(lat); // Set latitude
                    summary.setUpdateCount(1); // Initial count for today
                    weatherSummaryRepository.save(summary);
                } else {
                    // Update existing summary
                    WeatherSummary summary = optionalSummary.get();
                    // More accurate average temperature update
                    summary.setCurrentTemp(tempCelsius);
                    summary.setFeelsLike(feelsLike); // Update current temperature
                    summary.setPressure((int) pressure); // Update pressure
                    summary.setHumidity(humidity); // Update humidity
                    summary.setWindSpeed(windSpeed); // Update wind speed
                    summary.setAverageTemp(((summary.getAverageTemp() * summary.getUpdateCount()) + tempCelsius) / (summary.getUpdateCount() + 1));
                    summary.setMaxTemp(Math.max(summary.getMaxTemp(), tempCelsius));
                    summary.setMinTemp(Math.min(summary.getMinTemp(), tempCelsius));
                    summary.setLon(lon); // Update longitude
                    summary.setLat(lat); // Update latitude
                    summary.setUpdateCount(summary.getUpdateCount() + 1); // Increment update count
                    weatherSummaryRepository.save(summary);
                }

                // Check for alerts related to the current city
                List<Alert> alerts = alertRepository.findByCity(city);
                for (Alert alert : alerts) {
                    if (tempCelsius > alert.getThreshold()) {
                        emailAlertService.sendTemperatureAlert(alert.getEmail(), city, tempCelsius);
                        logger.info("Alert email sent to {} for city: {}. Current temperature: {}°C exceeds threshold: {}°C.", alert.getEmail(), city, tempCelsius, alert.getThreshold());
                    }
                }

                logger.info("Weather data for {} fetched successfully.", city);
            } else {
                logger.warn("Failed to fetch weather data for {}", city);
            }
        }
    }
}
