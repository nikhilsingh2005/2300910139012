Spring Boot URL Shortener Microservice 🚀
This is a comprehensive guide to the HTTP URL Shortener Microservice, a robust and scalable web application built with Spring Boot. The service provides core URL shortening functionality along with basic analytical capabilities, all while integrating a custom, external logging middleware.

The entire system is designed with a microservice architecture, ensuring a single, cohesive application handles all specified API endpoints. Authentication is pre-authorized, and the service focuses on its core task: providing a reliable and efficient URL shortening solution.

Key Features ✨
Custom Shortcodes: Users can provide a specific shortcode of their choice. The service validates its uniqueness and format. If a custom shortcode isn't provided, the system automatically generates a unique, alphanumeric code.

Default Validity: Short links default to a 30-minute validity period, but users can specify a custom duration in minutes.

URL Redirection: The microservice handles intelligent redirection. When a user accesses a shortened URL, they are automatically redirected to the original, long URL.

Usage Statistics: Detailed statistics for each shortened URL are tracked and can be retrieved via a dedicated API endpoint. This includes a total click count and detailed click events with timestamps, source, and a coarse geographical location.

Robust Error Handling: The API provides clear, descriptive JSON responses and appropriate HTTP status codes for invalid requests (e.g., malformed input, non-existent shortcodes, expired links, and shortcode collisions).

Mandatory Logging Integration: All application logs are sent to an external Log API using a custom-built logging middleware, ensuring no console logging or inbuilt language loggers are used.

Technologies Used 🛠️
Spring Boot: The core framework for building the microservice.

Spring Data JPA: For data persistence and repository management.

H2 Database: An in-memory database used for simplicity and ease of setup in development.

Maven: The project's build automation and dependency management tool.

Jakarta Validation: Used for validating incoming request payloads to ensure data integrity.

Custom Logging Middleware: A bespoke solution for logging, integrated as a reusable component.

Getting Started 🚀
Follow these simple steps to get the microservice up and running on your local machine.

1. Prerequisites
   Ensure you have the following installed:

Java 17+

Maven 3.6+

2. Clone the Repository
   Clone the project from its Git repository to your local machine:

Bash

git clone https://github.com/nikhilsingh2005/2300910139012
cd <your-project-directory>
3. Build and Run
   Use the Maven wrapper to build the project and start the Spring Boot application. This will download all necessary dependencies and run the service on http://localhost:8080.

Bash

./mvnw clean install
./mvnw spring-boot:run
API Endpoints 📋
Below is a detailed specification of the RESTful API endpoints exposed by the microservice.

1. Create a Short URL
   Endpoint: POST /shorturls

Description: Creates a new short URL.

Request Body Example:

JSON

{
"url": "https://www.example.com/a-very-long-and-descriptive-url",
"validity": 60,  // Optional: validity in minutes
"shortcode": "my-custom-code" // Optional: custom shortcode
}
Success Response: HTTP 201 Created

JSON

{
"shortLink": "http://localhost:8080/my-custom-code",
"expiry": "2025-09-10T14:30:58Z" // ISO 8601 formatted timestamp
}
2. Redirect
   Endpoint: GET /{shortcode}

Description: Redirects to the original URL.

Behavior: When accessed, the service performs an HTTP 302 Found redirect to the original URL.

3. Retrieve Short URL Statistics
   Endpoint: GET /shorturls/{shortcode}

Description: Retrieves detailed usage statistics for a specific shortcode.

Success Response: HTTP 200 OK

JSON

{
"totalClicks": 42,
"originalUrl": "https://www.example.com/a-very-long-and-descriptive-url",
"creationDate": "2025-09-10T14:30:58",
"expiryDate": "2025-09-10T15:30:58",
"detailedClicks": [
{
"shortcode": "my-custom-code",
"timestamp": "2025-09-10T14:31:05",
"referrer": "https://www.google.com",
"ipAddress": "172.21.10.1" // Coarse geographical location from IP
},
// ... more click events
]
}
Error Handling 🚨
The service provides informative error responses for common issues.

HTTP Status	Error Description	Example Scenario
400 Bad Request	Invalid input or malformed request body.	A field is missing or an invalid URL format is provided.
404 Not Found	The requested shortcode does not exist or has expired.	A user tries to access /non-existent-code.
409 Conflict	The requested custom shortcode is already in use.	A user attempts to create a link with a shortcode that already exists.
410 Gone	The requested shortcode has expired.	A user tries to access a link that is no longer valid.

Export to Sheets
Logging 🪵
All internal application events are logged using the custom logging middleware. This ensures a clean separation of concerns and allows for centralized log management and analysis. The logs are sent as a POST request to the configured external API with the following structure:

JSON

{
"stack": "backend",
"level": "error",
"package": "service",
"message": "A detailed error message or event description"
}