function redirectToMainPage() {
    const cityInput = document.getElementById("cityInput").value;
    const validCities = [
        "Delhi", "Jaipur", "Agra", "Varanasi", "Dehradun", "Chandigarh",
        "Mumbai", "Pune", "Ahmedabad", "Surat", "Nagpur", "Bangalore",
        "Chennai", "Hyderabad", "Coimbatore", "Thiruvananthapuram",
        "Kolkata", "Bhubaneswar", "Guwahati", "Patna", "Durgapur",
        "Bhopal", "Indore", "Shillong", "Imphal", "Agartala",
        "Aizawl", "Srinagar", "Leh", "Puducherry", "Mysuru",
        "Vadodara", "Jodhpur", "Nashik"
    ];

    // Check if the entered city is valid
    if (validCities.includes(cityInput)) {
        // Redirect to the main page with the selected city
        window.location.href = `main.html?city=${encodeURIComponent(cityInput)}`;
    } else {
        alert("Please enter a valid city from the list.");
    }
}

// Function to get the city from URL query parameters
function getCityFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('city');
}

// Fetch current weather data based on the city
function fetchWeatherData(city) {
    const apiUrl = `http://localhost:8086/api/weather/summaries/${city}`;

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data) && data.length > 0) {
                const weatherData = data[0];
                document.getElementById('currentCity').textContent = weatherData.city || "N/A";
                document.getElementById('currentDateTime').textContent = new Date().toLocaleString();
                document.getElementById('currentTemp').textContent = weatherData.currentTemp.toFixed(1) || "N/A";
                document.getElementById('feelsLike').textContent = weatherData.feelsLike.toFixed(1) || "N/A";
                document.getElementById('humidity').textContent = weatherData.humidity || "N/A";
                document.getElementById('dominantWeather').textContent = weatherData.dominantWeather || "N/A";
                const weatherImage = document.getElementById('weatherImage');

                // Set the weather image based on dominant weather
                switch (weatherData.dominantWeather.toLowerCase()) {
                    case 'clouds': weatherImage.src = 'https://cdn-icons-png.flaticon.com/128/414/414927.png'; break;
                    case 'haze': weatherImage.src = 'https://cdn-icons-png.flaticon.com/128/9361/9361636.png'; break;
                    case 'clear': weatherImage.src = 'https://cdn-icons-png.flaticon.com/128/6974/6974833.png'; break;
                    case 'mist': weatherImage.src = 'https://cdn-icons-png.flaticon.com/128/12276/12276483.png'; break;
                    case 'smoke': weatherImage.src = 'https://cdn-icons-png.flaticon.com/128/10206/10206854.png'; break;
                    default: weatherImage.src = ''; break;
                }

                document.getElementById('maxTemp').textContent = weatherData.maxTemp.toFixed(1) || "N/A";
                document.getElementById('minTemp').textContent = weatherData.minTemp.toFixed(1) || "N/A";
                document.getElementById('avgTemp').textContent = weatherData.averageTemp.toFixed(1) || "N/A";
            } else {
                alert('No data found for this city.');
            }
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
            alert('Failed to fetch weather data. Please check your API or city name.');
        });
}

function fetchWeatherForecast(city) {
    const apiUrl = `http://localhost:8086/api/weather/forecast/${city}`;

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const forecastBoxes = document.querySelector('.row'); // Reference the row where forecast boxes are located
            forecastBoxes.innerHTML = ''; // Clear previous data

            data.forEach((forecast) => {
                const forecastBox = document.createElement('div');
                forecastBox.className = 'col-lg-2 col-md-4 mb-4 forecast-box'; // Ensure the class names are consistent with Bootstrap

                // Get the date for each forecast (assuming forecast.date contains the date string)
                const date = new Date(forecast.date); // Convert to Date object
                const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
                const formattedDate = date.toLocaleDateString('en-US', options); // Format the date

                forecastBox.innerHTML = `
                    <h3>${formattedDate}</h3>
                    <p>Weather: <span>${forecast.weatherMain}</span></p>
                    <p>Min Temp: <span>${forecast.minTemp.toFixed(1)} °C</span></p>
                    <p>Max Temp: <span>${forecast.maxTemp.toFixed(1)} °C</span></p>
                `;
                forecastBoxes.appendChild(forecastBox);
            });
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

// Function to fetch data when the change location button is clicked
function changeLocation() {
    const city = document.getElementById('searchBar').value.trim();
    if (city) {
        fetchWeatherData(city);
        fetchWeatherForecast(city);
    } else {
        alert('Please enter a city name');
    }
}

// Fetch data when the page loads
window.onload = function() {
    const city = getCityFromUrl();
    if (city) {
        fetchWeatherData(city);
        fetchWeatherForecast(city);
    } else {
        alert('City not provided. Please go back and enter a city.');
    }
};

function showAlertForm() {
    $('#alertModal').modal('show'); // Use Bootstrap to show the modal
}

function setWeatherAlert() {
    const city = document.getElementById('alertCity').value.trim();
    const email = document.getElementById('alertEmail').value.trim();
    const threshold = parseFloat(document.getElementById('alertThreshold').value.trim());

    if (!city || !email || isNaN(threshold)) {
        alert('Please fill in all fields correctly.');
        return;
    }

    const apiUrl = 'http://localhost:8086/api/weather/set'; // Adjust to your correct URL

    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded' // or 'application/json' if you're sending JSON
        },
        body: new URLSearchParams({
            city: city,
            email: email,
            threshold: threshold
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.status);
        }
        return response.text();
    })
    .then(data => {
        alert(data); // Show success message
        // Optionally, reset the alert form fields here
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
        alert('Failed to set weather alert. Please check your input and try again.');
    });
}

// Modify the button's onclick function in the HTML
document.querySelector('.nav-container button').onclick = changeLocation;
