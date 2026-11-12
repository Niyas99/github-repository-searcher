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
- Maven (optional if using the Maven wrapper)
- Eclipse IDE or another Java IDE

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

Set the `DB_PASSWORD` environment variable to your PostgreSQL password before running the application.

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
git clone YOUR_GITHUB_REPOSITORY_URL
```

### 2. Navigate to the project directory

```bash
cd github-repository-searcher
```

### 3. Run the application

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

**Swagger UI:**

```text
http://localhost:8086/swagger-ui/index.html
```

Open Swagger UI to explore and test the available REST endpoints.

## API Endpoints

### 1. Search GitHub Repositories

**Method:** POST

**Endpoint:** `/api/github/search`

Searches GitHub repositories using the provided query and saves the results in PostgreSQL.

**Example Request:**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars",
  "order": "desc"
}
```

### 2. Retrieve Stored Repositories

**Method:** GET

**Endpoint:** `/api/github/repositories`

Retrieves repositories stored in PostgreSQL.

**Supported Query Parameters:**

| Parameter | Description |
|---|---|
| `language` | Filter by programming language |
| `minStars` | Minimum number of stars |
| `sortBy` | Sort by stars, forks, or updatedAt |
| `order` | Sort order: asc or desc |

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

Run the tests using:

```bash
mvn clean test
```

Alternatively, use the Maven wrapper:

```powershell
.\mvnw.cmd clean test
```

## Error Handling

The application handles invalid input, unexpected GitHub API responses, and other application errors using centralized exception handling.

## Deployment

Deployment details will be added after publishing the application.

- **Base URL:** Add your deployed API URL here.
- **Swagger UI:** Add your deployed Swagger URL here.

## Author

**Niyas Mohammed Basheer**

**Project:** GitHub Repository Searcher