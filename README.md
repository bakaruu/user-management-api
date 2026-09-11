# 🧩 User Management API

A RESTful API focused on authentication and user management, built to reflect real backend architecture practices using Spring Boot.

This project demonstrates how to design a secure, scalable, and maintainable backend system with stateless authentication, clear separation of concerns, and database integration.

> 🧠 **Note:** This project is designed with real-world backend principles in mind. The focus is not just on making things work, but on structuring a system that remains clean, testable, and extensible as it grows.

---

## 🔗 Live Deployment

Live demo, Swagger docs, and full write-up: see the [portfolio case study](https://bakaru.dev/projects/user-management).

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
| 🧪 | JUnit 5 + Mockito + Spring MockMvc |

---

## ✨ Features

| | Feature |
|--|---------|
| 🔐 | Secure authentication with JWT (15-minute access tokens) |
| 🔁 | Revocable refresh tokens for persistent sessions (7-day lifetime) |
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
git clone https://github.com/bakaruu/user-management-api.git
cd user-management-api
```

**2. Configure environment variables**

Create a `.env` file in the project root:
```env
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=change-this-to-a-random-string-of-at-least-32-characters
```

> The JWT secret must be at least 32 characters long (HS256 requires a 256-bit key). The application fails fast at startup if it isn't.

**3. Start the database**
```bash
docker-compose up -d
```

**4. Run the application**
```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

**5. Access API documentation**
(http://localhost:8080/swagger-ui.html)

---

## 📡 API Endpoints

All request/response bodies below are JSON. Endpoints under **User** and **Admin** require the header:
```
Authorization: Bearer <access_token>
```

### 🔓 Public

#### `POST /auth/register`
Registers a new user with the `USER` role.

Request:
```json
{
  "firstName": "Ada",
  "lastName": "Lovelace",
  "dni": "12345678A",
  "email": "ada@example.com",
  "password": "password123"
}
```
> `dni` must match the Spanish DNI format: 8 digits followed by a letter (e.g. `12345678A`).

Response `201 Created`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "N3l1c2VyLXJlZnJlc2gtdG9rZW4...",
  "role": "USER",
  "user": {
    "id": "b3f1c2a0-...",
    "firstName": "Ada",
    "lastName": "Lovelace",
    "dni": "12345678A",
    "email": "ada@example.com",
    "role": "USER",
    "status": "ACTIVE",
    "createdAt": "2026-09-11T10:00:00",
    "updatedAt": "2026-09-11T10:00:00"
  }
}
```
`409 Conflict` if the email or DNI is already registered.

#### `POST /auth/login`
Request:
```json
{ "email": "ada@example.com", "password": "password123" }
```
Response `200 OK`: same shape as `/auth/register`.
`401 Unauthorized` for wrong credentials or a suspended account.

#### `POST /auth/refresh`
Exchanges a valid refresh token for a new 15-minute access token.

Request:
```json
{ "refreshToken": "N3l1c2VyLXJlZnJlc2gtdG9rZW4..." }
```
Response `200 OK`: same shape as `/auth/register` (the refresh token is returned unchanged).
`401 Unauthorized` if the refresh token is invalid, expired, or revoked.

#### `POST /auth/logout`
Revokes the given refresh token server-side.

Request:
```json
{ "refreshToken": "N3l1c2VyLXJlZnJlc2gtdG9rZW4..." }
```
Response: `204 No Content`.

### 👤 User

#### `GET /users/me`
Returns the authenticated user's profile. Response `200 OK`: a `user` object, same shape as in `/auth/register`.

#### `PUT /users/me`
Updates the authenticated user's own data. All fields are optional — only the ones provided are updated.

Request:
```json
{ "firstName": "Ada", "email": "ada.new@example.com" }
```
Response `200 OK`: the updated `user` object.

### 🔑 Admin
*Requires the `ADMIN` role.*

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/users` | List all users |
| `GET` | `/users/{id}` | Get user by ID |
| `PUT` | `/users/{id}` | Update any user (same body as `PUT /users/me`) |
| `PATCH` | `/users/{id}/status` | Suspend or activate a user |
| `DELETE` | `/users/{id}` | Delete a user |

`GET`/`PUT`/`DELETE` on `/users/{id}` return `404 Not Found` if the user doesn't exist. Admin accounts cannot be suspended or deleted (`403 Forbidden`).

#### `PATCH /users/{id}/status`
Request:
```json
{ "status": "SUSPENDED" }
```
`status` must be `ACTIVE` or `SUSPENDED`. Response: `204 No Content`.

### ⚠️ Error responses

All errors share the same shape:
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Email must be valid"
  }
}
```
`errors` is only present for field-level validation failures (`400`); other errors (`401`, `403`, `404`, `409`) omit it.

---

## 🧪 Running Tests
```bash
./mvnw test
```

Includes unit tests (Mockito) for service-layer business logic and integration tests (`@SpringBootTest` + MockMvc) that exercise the full security filter chain, covering both authenticated and unauthenticated request paths.

---

## 📄 License

MIT — see [LICENSE](LICENSE).
