package com.zeotap.weathermonitoring.Service;

import com.zeotap.weathermonitoring.Models.ForecastSummary;
import com.zeotap.weathermonitoring.Models.ForecastResponse;
import com.zeotap.weathermonitoring.Models.WeatherSummary;
import com.zeotap.weathermonitoring.Repository.ForecastSummaryRepository;
import com.zeotap.weathermonitoring.Repository.WeatherSummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ForecastScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ForecastScheduler.class);

    @Autowired
    private WeatherSummaryRepository weatherSummaryRepository;

    @Autowired
    private ForecastSummaryRepository forecastSummaryRepository;

    @Autowired
    private WeatherService weatherService; // Assuming you have a WeatherService for API calls

    /**
     * Scheduled method to fetch forecast data every 24 hours.
     */
    @Scheduled(fixedRate = 86400000) // Fetch every 24 hours
    public void fetchForecastData() {
        List<WeatherSummary> cities = weatherSummaryRepository.findAll(); // Get all cities with their lat/lon

        for (WeatherSummary summary : cities) {
            String city = summary.getCity();
            double lat = summary.getLat();
            double lon = summary.getLon();

            // Call the OpenWeatherMap forecast API
            ForecastResponse weatherResponse = weatherService.getForecast(lat, lon); // Implement this in WeatherService

            if (weatherResponse != null && weatherResponse.getList() != null) {
                for (int i = 0; i < 5; i++) { // Only take upcoming 5 days
                    // Get the forecast for 12:00 PM (you can adjust the index if needed)
                    int forecastIndex = i * 8 + 3; // 12:00 PM entry for each day (3rd entry in 8 data points per day)
                    if (weatherResponse.getList().size() > forecastIndex) { // Ensure there's enough data
                        var forecast = weatherResponse.getList().get(forecastIndex);
                        LocalDate date = LocalDate.now().plusDays(i); // Correctly set the date
                        double minTemp = weatherService.convertKelvinToCelsius(forecast.getMain().getTemp_min());
                        double maxTemp = weatherService.convertKelvinToCelsius(forecast.getMain().getTemp_max());
                        String weatherMain = forecast.getWeather().get(0).getMain();

                        // Check if the forecast summary already exists
                        ForecastSummary existingForecast = forecastSummaryRepository.findByCityAndDate(city, date);
                        if (existingForecast != null) {
                            // Update the existing forecast summary
                            existingForecast.setMinTemp(minTemp);
                            existingForecast.setMaxTemp(maxTemp);
                            existingForecast.setWeatherMain(weatherMain);
                            forecastSummaryRepository.save(existingForecast);
                            logger.info("Updated forecast data for {} on {}.", city, date);
                        } else {
                            // Create a new forecast summary if it does not exist
                            ForecastSummary forecastSummary = new ForecastSummary();
                            forecastSummary.setCity(city);
                            forecastSummary.setDate(date);
                            forecastSummary.setMinTemp(minTemp);
                            forecastSummary.setMaxTemp(maxTemp);
                            forecastSummary.setWeatherMain(weatherMain);

                            forecastSummaryRepository.save(forecastSummary);
                            logger.info("New forecast data for {} on {} fetched successfully.", city, date);
                        }
                    }
                }
            } else {
                logger.warn("Failed to fetch forecast data for {}", city);
            }
        }
    }
}
