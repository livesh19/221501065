# URL Shortener Microservice

A robust HTTP URL Shortener Microservice built with Java and Spring Boot. This service provides core URL shortening functionality with basic analytics and **mandatory custom logging** as per the evaluation requirements.

## Features

- **Shorten URLs:** Generate globally unique short links for long URLs.
- **Custom Shortcodes:** Optionally specify your own shortcode for a short URL.
- **Default Validity:** Short links expire after 30 minutes by default (customizable per request).
- **Redirection:** Accessing a short link redirects to the original URL (if not expired).
- **Robust Error Handling:** Returns clear JSON errors for invalid input, expired/nonexistent codes, and more.
- **Mandatory Logging:** All actions are logged using a custom HTTP-based logging middleware (no built-in loggers).
- **No Authentication:** APIs are pre-authorized for evaluation; no login/registration required.

## Technology Stack

- Java 17
- Spring Boot 3.x
- In-memory storage (ConcurrentHashMap)

## Requirements

- Java 17+
- Maven 3.6+

## Setup & Run

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd urlShort
   ```
2. **Build the project:**
   ```sh
   mvn clean install
   ```
3. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The service will start on `http://localhost:8080` by default.

## API Endpoints

### 1. Create Short URL

- **Endpoint:** `POST /shorturls`
- **Request Body:**
  ```json
  {
    "url": "https://example.com/very/long/url",
    "validity": 30,           // (optional, in minutes, defaults to 30)
    "shortcode": "custom1"    // (optional, custom shortcode)
  }
  ```
- **Response (201):**
  ```json
  {
    "shortLink": "https://hostname:port/custom1",
    "expiry": "2025-01-01T00:30:00Z"
  }
  ```
- **Errors:**
  - 400 Bad Request for invalid input, duplicate shortcode, etc.

### 2. Redirect to Original URL

- **Endpoint:** `GET /{shortcode}`
- **Behavior:** Redirects to the original URL if the shortcode exists and is not expired.
- **Errors:**
  - 400 Bad Request for expired or non-existent shortcodes.

## Logging

- All requests, errors, and key actions are logged using a custom HTTP-based logging middleware (`LogMiddleware`).
- No use of built-in loggers or console logging for business events.
- Logs are sent to a remote evaluation endpoint as required.

## Project Structure

```
src/main/java/com/projext/urlShort/
├── controller/UrlShortenerController.java   # REST API endpoints
├── service/UrlShortenerService.java         # Business logic
├── model/ShortenRequest.java                # Request DTO
├── model/ShortenResponse.java               # Response DTO
├── model/UrlMapping.java                    # In-memory entity
├── exception/GlobalExceptionHandler.java    # Error handling
├── UrlShortApplication.java                 # Main Spring Boot app
loggingMiddleware/LogMiddleware.java         # Custom logging middleware
```

## Notes

- This project is for evaluation/demo purposes and uses in-memory storage (no database).
- All API endpoints are open (no authentication).
- Logging is mandatory and handled via the provided middleware.

