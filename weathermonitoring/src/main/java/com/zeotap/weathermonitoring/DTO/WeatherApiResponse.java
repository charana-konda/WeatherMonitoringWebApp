package com.zeotap.weathermonitoring.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherApiResponse {

    private Coordinates coord;

    private Main main;

    @JsonProperty("weather")
    private Weather[] weather;

    private String name; // City name

    @Data
    public static class Coordinates {
        private double lon;
        private double lat;
    }

    @Data
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }
}
