# Library Management System

A Spring Boot web application for managing a library's books, students, and book-issuing operations. The project exposes a RESTful API backed by MySQL, with a Thymeleaf front-end currently under development.

\---

## Tech Stack

|Layer|Technology|
|-|-|
|Language|Java 25|
|Framework|Spring Boot 4.0.6|
|Web|Spring MVC (`spring-boot-starter-webmvc`)|
|Persistence|Spring Data JPA + Hibernate|
|Database (prod)|MySQL 8+|
|Database (test)|H2 (in-memory)|
|View Layer|Thymeleaf *(in progress)*|
|Object Mapping|MapStruct 1.6.3|
|Boilerplate reduction|Lombok|
|Validation|Jakarta Bean Validation (`spring-boot-starter-validation`)|
|Testing|JUnit 5, Mockito, Spring MockMvc|
|Build|Maven (Maven Wrapper included)|

\---

## Project Structure

```
src/
├── main/
│   ├── java/com/thokozanimahlangu/
│   │   ├── bootstrap/          # BootStrapData – seeds the DB on startup
│   │   ├── controllers/        # REST controllers (Book, Student) + NotFoundException
│   │   ├── entities/           # JPA entities: Book, Student, IssueBook
│   │   ├── models/             # DTOs: BookDTO, StudentDTO
│   │   ├── mappers/            # MapStruct interfaces: BookMapper, StudentMapper
│   │   ├── repositories/       # Spring Data repositories + JPA Specifications
│   │   └── services/           # Service interfaces + JPA implementations
│   └── resources/
│       ├── application.properties
│       ├── static/             # CSS / JS assets (planned)
│       └── templates/          # Thymeleaf templates (planned)
└── test/
    └── java/com/thokozanimahlangu/
        ├── bootstrap/          # BootStrapDataTest
        └── controllers/        # BookControllerTest (MockMvc)
```

\---

## Domain Model

### Book

|Field|Type|Constraints|
|-|-|-|
|id|UUID|Primary key, auto-generated|
|title|String|`@NotBlank`, max 100 chars|
|author|String|`@NotBlank`, max 250 chars|
|isbn|String|`@NotBlank`, max 17 chars|
|publicationYear|Integer|`@NotNull`|
|createdDate|LocalDateTime|Auto-set on insert|
|updateDate|LocalDateTime|Auto-updated|

### Student

|Field|Type|Constraints|
|-|-|-|
|id|UUID|Primary key, auto-generated|
|firstName|String|`@NotBlank`, max 100 chars|
|lastName|String|`@NotBlank`, max 100 chars|
|email|String|`@NotBlank`, max 100 chars|
|createdDate|LocalDateTime|Auto-set on insert|
|updateDate|LocalDateTime|Auto-updated|

### IssueBook

Tracks which student has borrowed which book, along with issue date, due date, and return date. References `Book` and `Student` via foreign keys.

\---

## REST API

### Books — `/api/v1/book`

|Method|Endpoint|Description|Response|
|-|-|-|-|
|`GET`|`/api/v1/book`|List all books (filterable)|`200 OK`|
|`GET`|`/api/v1/book/{bookId}`|Get a single book by UUID|`200 OK` / `404`|
|`POST`|`/api/v1/book`|Create a new book|`201 Created` + `Location` header|
|`PUT`|`/api/v1/book/{bookId}`|Full update of a book|`204 No Content` / `404`|
|`PATCH`|`/api/v1/book/{bookId}`|Partial update of a book|`204 No Content`|
|`DELETE`|`/api/v1/book/{bookId}`|Delete a book|`204 No Content` / `404`|

**Query parameters for `GET /api/book`:** `title`, `author`, `isbn`, `publicationYear`

### Students — `/api/v1/student`

|Method|Endpoint|Description|Response|
|-|-|-|-|
|`GET`|`/api/v1/student`|List all students (filterable)|`200 OK`|
|`GET`|`/api/v1/student/{studentId}`|Get a single student by UUID|`200 OK` / `404`|
|`POST`|`/api/v1/student`|Create a new student|`201 Created`|
|`PUT`|`/api/v1/student/{studentId}`|Full update of a student|`204 No Content` / `404`|
|`DELETE`|`/api/v1/student/{studentId}`|Delete a student|`204 No Content` / `404`|

**Query parameters for `GET /api/student`:** `firstName`, `lastName`, `email`

\---

## Database Setup

The project targets **MySQL**. A setup script is provided at the project root.

```sql
-- Run this once to create the database and tables
source SetUpSQL.sql
```

This creates the `LMSdb` database and the `Book`, `Student`, and `IssueBook` tables with the correct schema and foreign-key constraints.

Configure your connection in `src/main/resources/application.properties`:

```properties
spring.application.name=Library-Management-System

spring.datasource.url=jdbc:mysql://localhost:3306/LMSdb
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
```

\---

## Getting Started

### Prerequisites

* Java 25+
* Maven 3.9+ (or use the included `./mvnw` wrapper)
* MySQL 8+

### Run the application

```bash
# Clone the repository
git clone git@github.com:johnmahlangu/Library-Management-System-SpringBoot.git
cd Library-Management-System

# Set up the database (see Database Setup above)

# Build and run
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

\---

## Running Tests

Tests use an in-memory H2 database and Spring MockMvc — no MySQL instance required.

```bash
./mvnw test
```

Current test coverage includes:

* `BookControllerTest` — MockMvc slice tests for all six book endpoints (GET by ID, GET list, POST, PUT, PATCH, DELETE), including the `404 Not Found` path.
* `BootStrapDataTest` — verifies that seed data loads correctly on startup.

\---

## Roadmap

* \[x] JPA entities (Book, Student, IssueBook)
* \[x] Repository layer with JPA Specifications for dynamic filtering
* \[x] Service layer (interface + JPA implementation)
* \[x] REST controllers (Book, Student)
* \[x] MapStruct mappers (Book, Student)
* \[x] Bootstrap seed data
* \[ ] **Unit tests** *(in progress)*
* \[ ] **MapStruct mapping** *(up next)*
* \[ ] **Thymeleaf front-end** (HTML \& CSS) *(planned)*

\---

## Author

Thokozani Mahlangu

