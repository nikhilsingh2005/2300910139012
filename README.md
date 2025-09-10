# HTTP URL Shortener Microservice 🚀

A lightweight and scalable **URL Shortener** built with **Spring Boot**.  
It provides URL shortening, redirection, usage statistics, and integrates a **custom logging middleware**.

---

## ✨ Features
- Create short URLs with optional **custom shortcodes**.
- Default validity of **30 minutes** (customizable).
- **Redirection** to the original URL via short links.
- **Usage statistics**: total clicks & detailed click logs.
- **Error handling** with clear JSON responses & status codes.
- External **logging middleware** (no console/inbuilt loggers).

---

## 📌 API Endpoints

### 1. Create Short URL
**POST** `/shorturls`
```json
{
  "url": "https://www.example.com/very-long-url",
  "validity": 60,
  "shortcode": "custom-code"
}
✅ Returns shortened URL + expiry timestamp.

2. Redirect
GET /{shortcode}
🔗 Redirects to the original URL (HTTP 302 Found).

3. URL Statistics
GET /shorturls/{shortcode}
📊 Returns original URL, expiry, total clicks, and click details.

🛠️ Tech Stack
Spring Boot

Spring Data JPA + H2 (in-memory DB)

Jakarta Validation

Maven

Custom Logging Middleware

🚀 Getting Started
bash
Copy code
git clone https://github.com/nikhilsingh2005/2300910139012
cd <your-project-directory>
./mvnw clean install
./mvnw spring-boot:run
Service runs at 👉 http://localhost:8080