package com.zeotap.weathermonitoring.Models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing the summary of weather data for a specific city and date.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weatherdatabb")
public class WeatherSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "City")
    private String city;

    @Column(name = "Average Temp")
    private double averageTemp;

    @Column(name = "Max Temp")
    private double maxTemp;

    @Column(name = "Min Temp")
    private double minTemp;

    @Column(name = "Dominant Weather")
    private String dominantWeather;

    @Column(name = "Date")
    private LocalDate date;

   // Track how many times the weather data has been updated
    
     @Column(name = "Current Temp")
     private double currentTemp;

     @Column(name = "Pressure")
     private int pressure;

     @Column(name = "Humidity")
     private int humidity;

     @Column(name = "Wind Speed")
     private double windSpeed;

     @Column(name = "FeelsLike")
     private double feelsLike;

     @Column(name = "Lon")
     private double lon;  // Add longitude field
 
     @Column(name = "Lat")
     private double lat;  // Add latitude field
 

    private int updateCount;
}