# 🔐 KAMS – Key & Asset Management System

A secure, scalable backend system for managing **venue keys, assets, borrowing workflows, and condition tracking** within institutions (e.g., universities).
<img width="1024" height="1536" alt="ChatGPT Image Apr 23, 2026, 05_57_18 AM" src="https://github.com/user-attachments/assets/61cb36e5-3c72-4aed-a99c-e39f716aefe2" />

---

## 🚀 Overview

KAMS digitizes the process of:

* Managing buildings (stations), floors, and venue structures
* Registering and tracking assets inside venues
* Borrowing and returning keys/assets using tracking IDs
* Role-based access control (Admin, Student, Instructor, Operator, Repairman)
* Monitoring asset/venue condition and repair workflows

---

## 🧠 Core Features

### 🏢 Venue Structure

* Stations (Buildings)
* Floors (Basement, Ground, Nth)
* Wings/Partitions (A, B, Left, Right, etc.)
* Venues (e.g., 201A, 101B)
* Assets within venues

---

### 📦 Asset Management

* Unique **tracking ID per asset**
* Condition states:

  * `GOOD`
  * `MODERATE`
  * `BAD`
  * `UNDER_REPAIR`
* Asset-based venue condition aggregation

---

### 🔑 Borrowing System

* Request assets or venue keys
* Auto-generated **request tracking number**
* Instructor approval required for student requests
* Station operator verifies and issues items

---

<img width="1968" height="958" alt="assets key" src="https://github.com/user-attachments/assets/401f77d2-3c45-4d5c-9b51-1b9ff3bf61b0" />
### 🛠 Repair Workflow

* Damaged assets reported with images
* Assigned to repairman
* Managed under station supervisor

---

### 📊 Reporting

* Weekly / Monthly / Yearly reports
* Track:

  * Damaged returns
  * Usage patterns
  * Repair statistics

---

## 👥 User Roles

| Role             | Permissions              |
| ---------------- | ------------------------ |
| Admin            | Full system control      |
| Student          | Request assets/venues    |
| Instructor       | Approve student requests |
| Station Operator | Issue/verify keys/assets |
| Repairman        | Handle repairs           |

---

## 🔐 Authentication & Security

* JWT-based authentication
* Role-based authorization (RBAC)
* Stateless session management
* Secure password hashing (BCrypt)

---

## 🏗 Architecture

### 📁 Project Structure

```
core/
 ├── config/
 │    └── security/
 │         ├── SecurityConfig.java
 │         ├── JwtConfig.java
 │         ├── AuthConfig.java
 │
 ├── security/
 │         └── JwtService.java
 │
 ├── context/
 │         └── CurrentUserProvider.java

features/
 ├── auth/
 ├── users/
 ├── venue/
 ├── asset/
 ├── request/
 ├── repair/
 └── reporting/
```

---

## 🔐 Security Architecture

| Component           | Responsibility        |
| ------------------- | --------------------- |
| SecurityConfig      | HTTP security rules   |
| JwtConfig           | JWT encoder/decoder   |
| AuthConfig          | Password encoding     |
| JwtService          | Token lifecycle       |
| CurrentUserProvider | Logged-in user access |

---

## ⚙️ Tech Stack

* **Java 21**
* **Spring Boot 3.5**
* Spring Security (JWT / OAuth2 Resource Server)
* Spring Data JPA (Hibernate)
* PostgreSQL
* Lombok
* Swagger (OpenAPI)
* WebSocket (real-time features)
* AWS S3 / Cloudflare R2 (file storage)
* Stripe (payments, optional)
* OpenHTMLtoPDF (report generation)

---

## 🔑 JWT Design

Token contains:

```json
{
  "sub": "REG12345",
  "userId": 1,
  "role": "STUDENT",
  "exp": "...",
  "jti": "unique-token-id"
}
```

---

## 🔧 Setup Instructions

### 1. Clone Project

```bash
git clone https://github.com/your-repo/kams.git
cd kams
```

---

### 2. Configure Environment

Create `.env` or set environment variable:

```bash
JWT_SECRET=your_base64_secret
```

---

### 3. Configure `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kams
    username: postgres
    password: password

jwt:
  secret: ${JWT_SECRET}
```

---

### 4. Run Application

```bash
mvn spring-boot:run
```

---

## 📡 API Documentation

* Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Example Authentication Flow

1. User logs in → receives JWT
2. JWT sent in header:

```
Authorization: Bearer <token>
```

3. Backend validates token
4. SecurityContext populated
5. Access granted based on role

---

## 📌 Future Improvements

* Redis-based JWT 
blacklist
* Multi-device session management
* Audit logging system
* Microservices architecture
* Notification system (email/SMS)

## 📄 License

This project is proprietary to **dax**.

---

## 👨‍💻 Author

Developed by **Dax (jermanus peter and charles lekia) in accomplish of their final year project at mbeya university of science and technology**
