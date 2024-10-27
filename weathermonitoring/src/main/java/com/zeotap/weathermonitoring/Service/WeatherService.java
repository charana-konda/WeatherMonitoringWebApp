package com.zeotap.weathermonitoring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zeotap.weathermonitoring.Models.ForecastResponse;
import com.zeotap.weathermonitoring.Models.WeatherResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for interacting with the OpenWeatherMap API to fetch weather data.
 */
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final String API_KEY = "828ed7ae884e12fb56928f8bbeacda06"; // Fetch API key from environment variable
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid=" + API_KEY;
    private static final String FORECAST_API_URL = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid=" + API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetch weather data from the OpenWeatherMap API for the given city name.
     *
     * @param city name of the city
     * @return WeatherResponse containing weather data or null if the fetch fails
     */
    public WeatherResponse getWeather(String city) {
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(WEATHER_API_URL, WeatherResponse.class, city);

        // Check if the response status code indicates success (2xx)
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // Return the body if the response is successful
        } else {
            logger.error("Failed to fetch weather data for city {}: {}", city, response.getStatusCode());
            return null; // Return null if the API call was unsuccessful
        }
    }

    /**
     * Fetch forecast data from the OpenWeatherMap API for the given latitude and longitude.
     *
     * @param lat latitude of the location
     * @param lon longitude of the location
     * @return ForecastResponse containing forecast data or null if the fetch fails
     */
    public ForecastResponse getForecast(double lat, double lon) {
        ResponseEntity<ForecastResponse> response = restTemplate.getForEntity(FORECAST_API_URL, ForecastResponse.class, lat, lon);

        // Check if the response status code indicates success (2xx)
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // Return the body if the response is successful
        } else {
            logger.error("Failed to fetch forecast data for lat: {}, lon: {}: {}", lat, lon, response.getStatusCode());
            return null; // Return null if the API call was unsuccessful
        }
    }

    /**
     * Method to convert temperature from Kelvin to Celsius.
     *
     * @param kelvin temperature in Kelvin
     * @return temperature in Celsius
     */
    public double convertKelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    /**
     * Method to convert temperature from Kelvin to Fahrenheit.
     *
     * @param kelvin temperature in Kelvin
     * @return temperature in Fahrenheit
     */
    public double convertKelvinToFahrenheit(double kelvin) {
        return (kelvin - 273.15) * 9 / 5 + 32;
    }
}
