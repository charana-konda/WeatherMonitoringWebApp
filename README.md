## Real-Time Data Processing System for Weather Monitoring with Rollups and Aggregates

This project provides a web application for users to access real-time weather updates and forecasts for various cities. Users can:

- **Search for Cities:** Find current weather conditions for specific locations.
- **View Forecasts:** Plan ahead by seeing predicted weather patterns.
- **Set Temperature Alerts (Optional):** Receive email notifications when temperatures reach designated thresholds.

The application prioritizes user experience by offering:

- **Intuitive Interface:** Easy navigation and clear information presentation.
- **Reliable Weather Data:** Access to accurate weather forecasts.

## System Components

The project consists of these core components:

- **Frontend:** Built with HTML, CSS, JavaScript, and Bootstrap for user interaction and visual appeal.
- **Backend:** Developed using Spring Boot (Maven) for handling API requests, business logic, data processing, and real-time data ingestion.
- **Database (PostgreSQL):** Stores user information, alert configurations, and weather data.
  - **Tables:**
    - `alert` (stores user emails and alert thresholds)
    - `forecast` (updated daily at 12:00 AM with comprehensive forecasts)
    - `current_summary` (updated every 5 minutes with real-time weather data)
- **Language:** Java 17

## Working Model Demo
https://drive.google.com/file/d/1Ss6HDhyh8h7BVFwe05Zh_KL7MbL-Lqx4/view?usp=sharing

## Setup and Installation

### Backend Setup (Spring Boot)

**Prerequisites:**

- Git client installed
- Maven installed

1. **Clone the Repository:**

   ```bash
   git clone https://your-username/weather-forecasting-website.git
   ```

2. **Navigate to Backend Directory:**

   ```bash
   cd weather-forecasting-website/backend
   ```

3. **Install Dependencies:**

   ```bash
   mvn install
   ```

4. **Run the Spring Boot Application:**

   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup (Optional, for local development)

1. **Navigate to Frontend Directory:**

   ```bash
   cd weather-forecasting-website/frontend
   ```

2. **Open Frontend in Browser:**

   Open `index.html` in your web browser to view the application locally (**Note:** Backend needs to be running for data access).

### Testing with Postman (Optional)

1. **Start Backend Application:**
   Ensure the Spring Boot application is running (`mvn spring-boot:run`).

2. **Open Postman:**
   Use Postman for API testing.

3. **Test Endpoints:**

   - **Get Current Weather:**
     - Method: GET
     - URL: `http://localhost:8080/api/weather/current?city={city_name}` (replace `{city_name}` with the desired city)

   - **Get Forecast Weather:**
     - Method: GET
     - URL: `http://localhost:8084/api/weather/forecast?city={city_name}` (replace `{city_name}` with the desired city)

   - **Set Weather Alert (Optional, if implemented):**
     - Method: POST
     - URL: `http://localhost:8080/api/alerts/set`
     - Body: JSON
       ```json
       {
         "city": "City Name",
         "email": "user@example.com",
         "threshold": "Temperature"
       }
       ```

4. **Check Responses:**
   Verify API responses to confirm correct functionality.

## Future Enhancements

- **More Security:** Implement robust security measures to protect user data and prevent unauthorized access.
- **Advanced Alerting System:** Customize thresholds, notification frequency, and delivery channels (SMS, push notifications).
- **Mobile Compatibility:** Develop a mobile app for on-the-go access.

## Conclusion

This weather forecasting website provides a valuable tool for users to stay informed about current and future weather conditions. The user-friendly interface and reliable data ensure a positive experience. Further enhancements are planned to offer even more comprehensive weather information and personalized alerts.
Absolutely! Here's the refined README.md incorporating the real-time data processing, security, database details, and additional future enhancements:

