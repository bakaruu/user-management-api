# 🧩 User Management API

A production-ready RESTful API focused on authentication and user management, built to reflect real backend architecture practices using Spring Boot.

This project demonstrates how to design a secure, scalable, and maintainable backend system with stateless authentication, clear separation of concerns, and database integration.

> 🧠 **Note:** This project is designed with real-world backend principles in mind. The focus is not just on making things work, but on structuring a system that remains clean, testable, and extensible as it grows.

---

## ⚙️ Technologies Used

| | Technology |
|--|------------|
| ☕ | Java 21 |
| 🚀 | Spring Boot 3.5.12 |
| 🔐 | Spring Security + JWT (stateless authentication) |
| 🗄️ | Spring Data JPA + Hibernate |
| 🐘 | PostgreSQL (production) + H2 (development/testing) |
| 🔄 | Flyway (database migrations) |
| 🐳 | Docker + Docker Compose |
| 📄 | SpringDoc OpenAPI (Swagger UI) |
| ⚡ | Lombok |
| 🧪 | JUnit 5 + Mockito |

---

## ✨ Features

| | Feature |
|--|---------|
| 🔐 | Secure authentication with JWT |
| 👥 | Role-based access control (USER / ADMIN) |
| 🧾 | User registration and login |
| 🛠️ | User profile management |
| 🧑‍💼 | Admin-level user control (update, suspend, delete) |
| 📊 | Account status handling (ACTIVE / SUSPENDED) |
| ⚠️ | Centralized exception handling with proper HTTP responses |
| ✅ | Validation on all incoming data |
| 🌐 | Interactive API documentation with Swagger |

---

## 🚀 Getting Started

**1. Clone the repository**
```bash
git clone https://github.com/your-username/user-management-api.git
cd user-management-api
```

**2. Configure environment variables**

Create a `.env` file in the project root:
```env
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-secret-key
```

**3. Start the database**
```bash
docker-compose up -d
```

**4. Run the application**
```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

**5. Access API documentation**
```
http://localhost:8080/swagger-ui.html
```

---

## 📡 API Endpoints

### 🔓 Public

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/register` | Register a new user |
| `POST` | `/auth/login` | Login and receive JWT |

### 👤 User

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/users/me` | Get own profile |
| `PUT` | `/users/me` | Update own profile |

### 🔑 Admin

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/users` | List all users |
| `GET` | `/users/{id}` | Get user by ID |
| `PUT` | `/users/{id}` | Update any user |
| `PATCH` | `/users/{id}/status` | Suspend or activate a user |
| `DELETE` | `/users/{id}` | Delete a user |

---

## 🧪 Running Tests
```bash
./mvnw test
```