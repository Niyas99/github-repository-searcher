# GitHub Repository Searcher

A Spring Boot REST API application that searches GitHub repositories using the GitHub REST API, stores repository information in PostgreSQL, and provides filtering and sorting capabilities.

## Technologies Used

- Java 21
- Spring Boot 4.1.2
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- GitHub REST API
- RestClient
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Docker

## Features

- Search GitHub repositories using a search query.
- Filter GitHub search results by programming language.
- Sort search results by stars, forks, or last updated date.
- Store repository information in PostgreSQL.
- Avoid duplicate records using the GitHub repository ID as the primary key.
- Retrieve stored repositories from the database.
- Filter stored repositories by language and minimum stars.
- Sort stored repositories in ascending or descending order.
- Validate request parameters and handle errors using centralized exception handling.

## Project Structure

```text
github-repository-searcher/
│
├── Dockerfile
├── src/
│   ├── main/
│   │   ├── java/com/niyas/githubsearcher/
│   │   │   ├── client/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Prerequisites

Make sure the following are installed:

- Java 21 or later
- PostgreSQL
- Git
- Eclipse IDE or another Java IDE

Maven is optional because the project includes the Maven wrapper.

## Database Configuration

Create a PostgreSQL database:

```sql
CREATE DATABASE github_searcher;
```

Configure the database connection in `src/main/resources/application.properties`.

```properties
spring.application.name=github-repository-searcher
server.port=8086

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/github_searcher}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

### Environment Variables

The application uses environment variables to configure database connectivity.

| Variable | Description |
|---|---|
| `DB_URL` | JDBC PostgreSQL connection URL |
| `DB_USERNAME` | PostgreSQL database username |
| `DB_PASSWORD` | PostgreSQL database password |

**Windows PowerShell:**

```powershell
$env:DB_PASSWORD="your_postgres_password"
```

**Linux / macOS:**

```bash
export DB_PASSWORD="your_postgres_password"
```

The application uses the default database URL and username shown above if `DB_URL` and `DB_USERNAME` are not provided.

## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/Niyas99/github-repository-searcher.git
```

### 2. Navigate to the project directory

```bash
cd github-repository-searcher
```

### 3. Configure the database

Create the PostgreSQL database and configure the required environment variables.

### 4. Run the application

Using Maven:

```bash
mvn spring-boot:run
```

Alternatively, using the Maven wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The application will start at:

```text
http://localhost:8086
```

## API Documentation

Swagger UI provides interactive API documentation.

**Local Swagger UI:**

http://localhost:8086/swagger-ui/index.html

Open Swagger UI to explore and test the available REST endpoints.

## API Endpoints

### 1. Search GitHub Repositories

**Method:** `POST`

**Endpoint:**

```text
/api/github/search
```

Searches GitHub repositories using the provided query and saves the results in PostgreSQL.

**Example Request:**

```json
{
  "query": "spring boot"
}
```

### 2. Retrieve Stored Repositories

**Method:** `GET`

**Endpoint:**

```text
/api/github/repositories
```

Retrieves repositories stored in PostgreSQL.

**Supported Query Parameters:**

| Parameter | Description |
|---|---|
| `language` | Filter by programming language |
| `minStars` | Minimum number of stars |
| `sortBy` | Sort by `stars`, `forks`, or `updatedAt` |
| `order` | Sort order: `asc` or `desc` |

**Example Request:**

```text
GET /api/github/repositories?language=Java&minStars=100&sortBy=stars&order=desc
```

## Testing

The project includes unit and integration tests to verify:

- GitHub API client functionality.
- Repository persistence.
- Filtering by programming language.
- Filtering by minimum stars.
- Sorting repositories.
- Database integration.
- Controller functionality.

Run the tests using Maven:

```bash
mvn clean test
```

Alternatively, use the Maven wrapper on Windows:

```powershell
.\mvnw.cmd clean test
```

On Linux or macOS:

```bash
./mvnw clean test
```

## Error Handling

The application handles:

- Invalid request parameters.
- Validation errors.
- Unexpected GitHub API responses.
- Database and application errors.

Centralized exception handling returns appropriate HTTP status codes and error messages.

## Deployment

The application is deployed on **Render**.

- **GitHub Repository:** [https://github.com/Niyas99/github-repository-searcher](https://github.com/Niyas99/github-repository-searcher)
- **Live API Base URL:** [https://github-repository-searcher-0bg4.onrender.com](https://github-repository-searcher-0bg4.onrender.com)
- **Swagger UI:** [https://github-repository-searcher-0bg4.onrender.com/swagger-ui/index.html](https://github-repository-searcher-0bg4.onrender.com/swagger-ui/index.html)

The application uses a PostgreSQL database hosted on Render.

### Environment Variables

Configure the following environment variables in the Render Web Service:

| Variable | Description |
|---|---|
| `DB_URL` | JDBC-formatted PostgreSQL connection URL |
| `DB_USERNAME` | PostgreSQL database username |
| `DB_PASSWORD` | PostgreSQL database password |

**Note:** Use the Render Internal Database URL as the source for your connection details. For `DB_URL`, make sure the URL starts with `jdbc:postgresql://`. Use the username and password in their separate environment variables.

The application is deployed using Docker.

## Author

**Niyas Mohammed Basheer**

**Project:** GitHub Repository Searcher