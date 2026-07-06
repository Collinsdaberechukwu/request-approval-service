# Request Approval Service

## Overview

The Request Approval Service is a Spring Boot application that manages the lifecycle of employee requests before they are approved. It provides endpoints for creating, updating, retrieving, deleting, and listing requests while enforcing the business rules around who can create a request and when it can be modified.

The service was designed with a clear separation of responsibilities by using service, repository, mapper, and DTO layers to keep the codebase easy to maintain and extend.

---

## Features

* Create a new request
* Retrieve a request by its ID
* Update an existing request
* Delete a request
* View pending requests with pagination
* Retrieve users and list all users
* Prevent duplicate pending requests
* Validate user eligibility before creating a request
* Consistent API response structure
* Transaction management for write operations
* Centralized exception handling

---

## Business Rules

The following rules are enforced by the application:

### Request Creation

* Only active users are allowed to create requests.
* Only users with the **EMPLOYEE** role can submit requests.
* A user cannot create another pending request with the same title.
* Every newly created request starts with:

  * **Status:** `PENDING`
  * **Initial Approval Level:** `MANAGER`

### Request Update

A request can only be updated while it is still in the **PENDING** state.

Once a request has entered the approval process or has already been approved, it can no longer be modified.

### Request Deletion

Only pending requests can be deleted.

Approved or processed requests are protected from deletion to preserve the approval history.

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

---

## Project Structure

```text
src
├── controller
├── dto
│   ├── Request
│   ├── Response
│   └── Update
├── enums
├── exception
├── mapper
├── model
├── repository
├── service
│   └── impl
└── config
```

The project follows a layered architecture where each layer has a single responsibility.

* **Controller** handles incoming requests.
* **Service** contains the business logic.
* **Repository** communicates with the database.
* **Mapper** converts entities to DTOs and vice versa.
* **DTOs** isolate API models from persistence models.

---

## API Endpoints

### Requests

| Method | Endpoint            | Description               |
| ------ | ------------------- | ------------------------- |
| POST   | `/requests`         | Create a new request      |
| GET    | `/requests/{id}`    | Retrieve a request        |
| PUT    | `/requests/{id}`    | Update a pending request  |
| DELETE | `/requests/{id}`    | Delete a pending request  |
| GET    | `/requests/pending` | Retrieve pending requests |

### Users

| Method | Endpoint      | Description        |
| ------ | ------------- | ------------------ |
| GET    | `/users/{id}` | Retrieve a user    |
| GET    | `/users`      | Retrieve all users |

---

## Validation

The service validates incoming requests before any data is persisted.

Examples include:

* User must exist.
* User must be active.
* User must have the EMPLOYEE role.
* Duplicate pending requests are not allowed.
* Only pending requests can be updated.
* Only pending requests can be deleted.

Whenever a validation fails, the service returns a meaningful error response describing the reason for the failure.

---

## Response Format

Every endpoint returns a consistent response structure.

```json
{
  "statusCode": 200,
  "statusMessage": "Request retrieved successfully",
  "data": {}
}
```

This keeps the API predictable and simplifies client-side integration.

---

## Logging

Application events are logged at the service layer to make troubleshooting easier.

Examples include:

* Request creation
* Request retrieval
* Pending request lookup
* User retrieval

Logging focuses on business events without exposing sensitive information.

---

## Running the Project

### Clone the repository

```bash
git clone <https://github.com/Collinsdaberechukwu/request-approval-service.git>
```

### Build the application

```bash
mvn clean install
```

### Run locally

```bash
mvn spring-boot:run
```

### Run with Docker

```bash
docker compose up --build
```

---

## Design Considerations

A few implementation decisions were made to improve reliability and maintainability:

* DTOs are used to avoid exposing entity models directly.
* Mapping logic is separated from business logic.
* Transactions are applied to write operations to maintain data consistency.
* Business validation is centralized within the service layer instead of controllers.
* Repository methods with locking are used where updates require concurrency protection.

---

## Future Improvements

Some areas that can be added in future iterations include:

* Authentication and authorization using JWT or OAuth2
* Role-based endpoint security with Spring Security
* Multi-level approval workflow
* Approval history and audit trail
* Email or event-based notifications
* Request search and filtering
* Unit and integration test coverage
* Request approval metrics and reporting

---

## Author

**Collins Okafor**

Backend Software Engineer

Specializing in building secure, scalable, and maintainable backend applications using Java and Spring Boot.

