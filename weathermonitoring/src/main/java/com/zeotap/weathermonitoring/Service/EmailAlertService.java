package com.zeotap.weathermonitoring.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailAlertService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTemperatureAlert(String email, String city, double currentTemp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Temperature Alert for " + city);
        message.setText("The temperature in " + city + " has exceeded your threshold. Current temperature: " + currentTemp + "°C.");
        mailSender.send(message);
    }
}
