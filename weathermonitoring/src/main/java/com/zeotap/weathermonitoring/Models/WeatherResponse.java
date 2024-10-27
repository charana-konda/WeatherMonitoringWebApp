package com.zeotap.weathermonitoring.Models;

import lombok.*;

import java.util.List;

/**
 * Class representing the weather response from the OpenWeatherMap API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private Coordinates coord;  // Add coordinates field
    private Main main;
    private Wind wind;
    private long dt;  // Date of the weather report
    private List<Weather> weather;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinates {
        private double lon;  // Longitude
        private double lat;  // Latitude
    }

    @Data
    public static class Main {
        private double temp;        // Temperature in Kelvin
        private double feels_like;  // Feels like temperature in Kelvin
        private int pressure;       // Atmospheric pressure
        private int humidity;       // Humidity percentage
    }

    @Data
    public static class Wind {
        private double speed;  // Wind speed
        private int deg;       // Wind direction (not used in this case)
    }

    @Data
    public static class Weather {
        private String main;        // Main weather description (e.g., Clear, Rain)
    }
}
