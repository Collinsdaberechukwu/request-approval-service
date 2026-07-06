# Request Approval Service

## Overview

The Request Approval Service is a Spring Boot application that manages the lifecycle of employee requests from submission through the approval process. It exposes REST endpoints for creating, retrieving, updating, deleting, and listing requests while enforcing the business rules that determine who can submit a request and when it can be modified.

The project follows a layered architecture with clear separation between controllers, services, repositories, mappers, and DTOs. This keeps the codebase maintainable, easier to test, and straightforward to extend as new requirements are introduced.

---

## Features

* Create a new request
* Retrieve a request by ID
* Update a pending request
* Delete a pending request
* Retrieve pending requests with pagination
* Retrieve a single user or list all users
* Prevent duplicate pending requests
* Validate user eligibility before request creation
* Consistent API response structure
* Transaction management for write operations
* Centralized exception handling

---

## Business Rules

### Request Creation

* Only active users can create requests.
* Only users with the **EMPLOYEE** role are allowed to submit requests.
* A user cannot create another pending request with the same title.
* Every new request is created with:

  * **Status:** `PENDING`
  * **Approval Level:** `MANAGER`

### Request Update

A request can only be updated while its status is **PENDING**. Once the approval process has started or the request has been approved, further modifications are not allowed.

### Request Deletion

Only requests that are still pending can be deleted. Processed or approved requests are retained to preserve the integrity of the approval history.

---

## Technology Stack

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok
* Docker
* Docker Compose
* Springdoc OpenAPI (Swagger)

---

## Project Structure

```text
src
├── controller
├── dto
│   ├── request
│   ├── response
│   └── update
├── enums
├── exception
├── mapper
├── model
├── repository
├── service
│   └── impl
└── config
```

The project follows a layered architecture where each layer has a clearly defined responsibility.

* **Controller** – Handles incoming HTTP requests and returns API responses.
* **Service** – Contains the application's business logic.
* **Repository** – Handles data access using Spring Data JPA.
* **Mapper** – Converts entities to DTOs and vice versa.
* **DTOs** – Define the request and response models exposed by the API.

---

## API Endpoints

### Request Endpoints

| Method | Endpoint            | Description               |
| ------ | ------------------- | ------------------------- |
| POST   | `/requests`         | Create a new request      |
| GET    | `/requests/{id}`    | Retrieve a request by ID  |
| PUT    | `/requests/{id}`    | Update a pending request  |
| DELETE | `/requests/{id}`    | Delete a pending request  |
| GET    | `/requests/pending` | Retrieve pending requests |

### User Endpoints

| Method | Endpoint      | Description        |
| ------ | ------------- | ------------------ |
| GET    | `/users/{id}` | Retrieve a user    |
| GET    | `/users`      | Retrieve all users |

---

## API Documentation

The project includes Swagger/OpenAPI documentation to make it easier to explore and test the available endpoints during development.

Once the application is running, the documentation can be accessed from:

**Swagger UI**

```text
http://localhost:8080/swagger-ui/index.html
```

**OpenAPI Specification**

```text
http://localhost:8080/v3/api-docs
```

The documentation provides:

* Available endpoints
* Request and response models
* HTTP status codes
* Request parameters
* Example payloads

This makes it easier to understand the API without referring directly to the source code.

---

## Validation

Incoming requests are validated before any data is written to the database.

Some of the validations include:

* User must exist.
* User must be active.
* User must have the **EMPLOYEE** role.
* Duplicate pending requests are not allowed.
* Only pending requests can be updated.
* Only pending requests can be deleted.

Whenever validation fails, the service returns a clear and meaningful error response describing the reason for the failure.

---

## Response Format

Every endpoint follows the same response structure.

```json
{
  "statusCode": 200,
  "statusMessage": "Request retrieved successfully",
  "data": {}
}
```

Keeping responses consistent makes it easier for clients to consume the API and handle responses in a predictable way.

---

## Logging

The service logs important application events to simplify monitoring and troubleshooting.

Examples include:

* Request creation
* Request retrieval
* Pending request lookup
* User retrieval

Logging is focused on business operations while avoiding the exposure of sensitive information.

---

## Running the Project

### Clone the Repository

```bash
git clone https://github.com/Collinsdaberechukwu/request-approval-service.git
```

### Build the Project

```bash
mvn clean install
```

### Run Locally

```bash
mvn spring-boot:run
```

### Run with Docker

```bash
docker compose up --build
```

---

## Design Considerations

A few implementation decisions were made to keep the application reliable and maintainable.

* DTOs are used to separate API models from persistence models.
* Mapping logic is isolated from business logic.
* Transactions are applied to write operations to maintain data consistency.
* Business rules are enforced within the service layer rather than controllers.
* Repository methods that require updates use locking where appropriate to reduce concurrency issues.

---

## Future Improvements

Some enhancements that could be introduced in future versions include:

* JWT or OAuth2 authentication
* Role-based authorization with Spring Security
* Multi-level approval workflow
* Approval history and audit trail
* Email or event-driven notifications
* Request search and filtering
* Unit and integration test coverage
* Request approval analytics and reporting

---

## Author

**Okafor Collins Daberechukwu**

Backend Software Engineer

Experienced in designing and building backend applications with Java and Spring Boot, with a focus on clean architecture, maintainable code, and scalable REST APIs.
