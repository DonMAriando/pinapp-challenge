# Challenge PinApp

A Java 21 Spring Boot application implementing Hexagonal Architecture (Ports & Adapters) for client management with PostgreSQL database support.

## Quick Start

**Option 1: Run everything with Docker (Recommended)**
```bash
# Build and start all services (app + database)
docker-compose up -d --build

# View logs
docker-compose logs -f app

# Access the API
# - Swagger UI: http://localhost:8080/swagger-ui.html
# - API Docs: http://localhost:8080/api-docs
# - Actuator Health: http://localhost:8080/actuator/health
# - pgAdmin: http://localhost:5050 (admin@challenge.com / admin)
```

**Option 2: Run only the database with Docker**
```bash
# 1. Start PostgreSQL with Docker
docker-compose up -d postgres

# 2. Run the application locally
mvn spring-boot:run

# 3. Access the API at http://localhost:8080
```

## Badges

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/DonMAriando/pinapp-challenge/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/DonMAriando/pinapp-challenge/tree/main)
[![Coverage Status](https://coveralls.io/repos/github/DonMAriando/pinapp-challenge/badge.svg?branch=main)](https://coveralls.io/github/DonMAriando/pinapp-challenge?branch=main)

## Architecture Overview

This application follows the **Hexagonal Architecture** pattern, which separates the core business logic from external concerns:

- **Domain Layer**: Contains pure business logic with no framework dependencies
- **Infrastructure Layer**: Contains adapters for external systems (REST API, Database)

### Project Structure

```
src/main/java/com/pinapp/challenge/
├── domain/                    # Core business logic
│   ├── model/               # Domain models (Client, ClientMetrics)
│   ├── port/
│   │   ├── in/             # Input ports (use cases)
│   │   └── out/            # Output ports (repository interfaces)
│   └── service/            # Domain services implementing use cases
└── infrastructure/         # External adapters
    ├── adapter/
    │   ├── in/rest/        # REST controllers and DTOs
    │   └── out/persistence/ # Database adapters
    └── config/             # Spring configuration
```

## Features

- ✅ Create new clients with validation
- ✅ List all clients with calculated life expectancy
- ✅ Get client metrics (average age, standard deviation)
- ✅ Spring Security with HTTP Basic authentication
- ✅ PostgreSQL database support
- ✅ H2 in-memory database for development
- ✅ Comprehensive unit tests
- ✅ **Swagger/OpenAPI documentation with interactive UI**

## API Endpoints

### 1. Create Client (Public)
```http
POST /api/clients
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez", 
  "edad": 30,
  "fechaNacimiento": "1994-01-15"
}
```

**Response:**
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "edad": 30,
  "fechaNacimiento": "1994-01-15",
  "fechaEsperadaFallecimiento": "2074-01-15"
}
```

### 2. Get All Clients (Secured)
```http
GET /api/clients
Authorization: Basic YWRtaW46cGFzc3dvcmQ=
```

**Response:**
```json
[
  {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "edad": 30,
    "fechaNacimiento": "1994-01-15",
    "fechaEsperadaFallecimiento": "2074-01-15"
  }
]
```

### 3. Get Client Metrics (Secured)
```http
GET /api/clients/metrics
Authorization: Basic YWRtaW46cGFzc3dvcmQ=
```

**Response:**
```json
{
  "promedioEdad": 35.5,
  "desviacionEstandar": 12.3,
  "totalClientes": 10
}
```

## Setup Instructions

### Prerequisites
- Java 21
- Maven 3.6+
- Docker & Docker Compose (for PostgreSQL)
- PostgreSQL (optional, if not using Docker)
- H2 Database (automatically included for development)

### Running the Application

1. **Clone and navigate to the project:**
   ```bash
   cd cursor
   ```

2. **Run with development profile (H2 database):**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

3. **Run with production profile (PostgreSQL):**
   ```bash
   mvn spring-boot:run
   ```

### Database Setup

#### Development (H2)
- No setup required - uses in-memory H2 database
- Access H2 console at: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

#### Production (PostgreSQL with Docker)

**Option 1: Using Docker Compose (Recommended)**

1. **Start PostgreSQL with Docker Compose:**
   ```bash
   docker-compose up -d
   ```

2. **Verify containers are running:**
   ```bash
   docker-compose ps
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access pgAdmin (Optional):**
   - URL: http://localhost:5050
   - Email: `admin@challenge.com`
   - Password: `admin`
   - Add server connection:
     - Host: `postgres`
     - Port: `5432`
     - Database: `clientdb`
     - Username: `postgres`
     - Password: `postgres`

5. **Stop the containers:**
   ```bash
   docker-compose down
   ```

6. **Remove containers and volumes:**
   ```bash
   docker-compose down -v
   ```

**Option 2: Manual PostgreSQL Installation**

1. Install PostgreSQL
2. Create database:
   ```sql
   CREATE DATABASE clientdb;
   ```
3. Update `application.properties` with your PostgreSQL credentials

**Database Configuration:**
- **Host:** localhost
- **Port:** 5432
- **Database:** clientdb
- **Username:** postgres
- **Password:** postgres

### API Authentication 🔐

**All API endpoints require HTTP Basic Authentication.**

**Default Credentials:**
- **Username:** `admin`
- **Password:** `password`

**Protected Endpoints:**
- ✅ All `/api/**` endpoints **require authentication**

**Public Endpoints (no authentication required):**
- `/swagger-ui/**` - API Documentation
- `/actuator/health` - Health checks
- `/h2-console` - H2 Database Console (dev profile only)

## API Documentation (Swagger)

The application includes comprehensive API documentation powered by Swagger/OpenAPI 3:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

### Health & Monitoring Endpoints (Actuator):

- **Health Check:** http://localhost:8080/actuator/health
- **Health Details:** http://localhost:8080/actuator/health (when authorized)
- **Application Info:** http://localhost:8080/actuator/info
- **Metrics:** http://localhost:8080/actuator/metrics

### Features:
- 📖 **Interactive API Documentation** - Test endpoints directly from the browser
- 🔐 **Authentication Support** - Built-in HTTP Basic auth testing
- 📝 **Request/Response Examples** - See sample data for all endpoints
- 🏷️ **Detailed Descriptions** - Complete parameter and response documentation
- 🎯 **Try It Out** - Execute API calls directly from the documentation

### Using Swagger UI:

1. **Navigate to** http://localhost:8080/swagger-ui.html

2. **Authenticate:**
   - Click the **"Authorize"** button (lock icon) at the top right
   - In the popup dialog, enter:
     - **Username:** `admin`
     - **Password:** `password`
   - Click **"Authorize"** button
   - Click **"Close"**
   - You should now see a closed lock icon (🔒) indicating you're authenticated

3. **Test endpoints:**
   - Click on any endpoint to expand it
   - Click **"Try it out"**
   - Fill in the required parameters
   - Click **"Execute"**
   - View the response below

4. **Note:** Without authentication, you'll receive `401 Unauthorized` responses for all API endpoints

## Testing

### Test Structure

The project includes three types of tests:

1. **Unit Tests** - Test individual components in isolation
2. **Integration Tests** - Test component interactions
3. **E2E Tests** - Test complete application flows from HTTP to database

### Running Tests

Run all tests:
```bash
mvn test
```

Run specific test classes:
```bash
mvn test -Dtest=ClientE2ETest
mvn test -Dtest=ClientServiceTest
```

Run all tests with coverage report:
```bash
mvn clean test
```

Run integration tests and verify coverage:
```bash
mvn verify
```

### Code Coverage (JaCoCo)

The project uses **JaCoCo** (Java Code Coverage) to ensure high-quality code with comprehensive test coverage.

#### Coverage Requirements
- **Instruction Coverage:** ≥ 90%
- **Branch Coverage:** ≥ 80%

#### Generating Coverage Reports

1. **Run tests with coverage:**
   ```bash
   mvn clean test jacoco:report
   ```

2. **View HTML coverage report:**
   - Open `target/site/jacoco/index.html` in your browser
   - Detailed coverage metrics for all packages and classes
   - Line-by-line coverage visualization

3. **Verify coverage thresholds:**
   ```bash
   # Run tests and check coverage
   mvn verify
   
   # Or check coverage on existing test results
   mvn verify -DskipTests
   ```
   This will fail the build if coverage is below the required thresholds.
   
   **Note:** The `jacoco:check` goal is bound to the `verify` phase in pom.xml. Running `mvn jacoco:check` directly from the command line will fail with a "parameters 'rules' are missing" error.

#### Coverage Report Structure
```
target/site/jacoco/
├── index.html              # Main coverage report
├── com.pinapp.challenge/   # Package-level reports
│   ├── domain/
│   │   ├── model/         # Domain models coverage
│   │   └── service/       # Service layer coverage
│   └── infrastructure/    # Infrastructure coverage
└── jacoco.csv             # Coverage data in CSV format
```

#### Current Coverage Statistics

| Package | Instruction Coverage | Branch Coverage |
|---------|---------------------|-----------------|
| **Domain Models** | 98% | 87% |
| **Domain Services** | 92% | 72% |
| **Infrastructure Config** | 100% | n/a |
| **REST Adapters** | 100% | n/a |
| **Persistence Adapters** | 100% | n/a |
| **Overall** | **90%** | **82%** |

#### Test Data Separation

Tests follow the **Test Data Builder** pattern with dedicated test data classes:
- `ClientTestData.java` - Domain model test data
- `ClientMetricsTestData.java` - Metrics test data  
- `CreateClientRequestTestData.java` - DTO request test data
- `ClientResponseTestData.java` - DTO response test data
- `ClientEntityTestData.java` - JPA entity test data

This approach provides:
- ✅ **Clean separation** between test logic and test data
- ✅ **Reusability** of test data across multiple test classes
- ✅ **Maintainability** - changes to test data in one place
- ✅ **Readability** - tests focus on behavior, not data setup

### End-to-End (E2E) Tests

Comprehensive E2E tests verify the complete application flow from HTTP requests through all layers to the database.

**Test Coverage:** `ClientE2ETest.java` (11 scenarios)

1. ✅ Create and retrieve client workflow
2. ✅ Multiple clients and metrics calculation
3. ✅ Authentication requirements
4. ✅ Input validation and error handling
5. ✅ Empty database scenarios
6. ✅ Complete workflow with calculations
7. ✅ Public Swagger endpoints
8. ✅ Health monitoring endpoints
9. ✅ Invalid credentials handling
10. ✅ Missing fields validation
11. ✅ Database persistence verification

**Run E2E tests:**
```bash
mvn test -Dtest=ClientE2ETest
```

## Business Logic

### Life Expectancy Calculation
The application calculates life expectancy by adding 80 years to the client's birth date:
```java
public LocalDate calculateLifeExpectancy() {
    return fechaNacimiento.plusYears(80);
}
```

### Metrics Calculation
- **Average Age:** Mean of all client ages
- **Standard Deviation:** Population standard deviation of ages
- **Total Clients:** Count of all registered clients

## Docker Support

This project includes Docker Compose configuration for easy database setup.

### Docker Compose Services

- **app**: Spring Boot application (Challenge PinApp API)
- **postgres**: PostgreSQL 16 database server
- **pgadmin**: Web-based database management tool (optional)

### Docker Commands

```bash
# Build and start all services (first time or after code changes)
docker-compose up -d --build

# Start all services (without rebuilding)
docker-compose up -d

# Start only specific services
docker-compose up -d postgres    # Only database
docker-compose up -d app postgres  # App and database

# View logs
docker-compose logs -f           # All services
docker-compose logs -f app       # Only app logs
docker-compose logs -f postgres  # Only database logs

# Stop all services
docker-compose down

# Stop and remove all data (including volumes)
docker-compose down -v

# Restart a specific service
docker-compose restart app
docker-compose restart postgres

# View running containers
docker-compose ps

# Access PostgreSQL CLI
docker exec -it challenge-postgres psql -U postgres -d clientdb

# Rebuild app after code changes
docker-compose up -d --build app

# View app container logs in real-time
docker logs -f challenge-app
```

### Docker Compose File Structure

```yaml
services:
  postgres:        # PostgreSQL 16 database
  pgadmin:         # Database management UI (port 5050)

volumes:
  postgres-data:   # Persistent database storage
  pgadmin-data:    # pgAdmin configuration storage

networks:
  challenge-network: # Isolated network for services
```

## Technology Stack

- **Java 21**
- **Spring Boot 3.3.4**
- **Spring Security**
- **Spring Data JPA**
- **Spring Boot Actuator** (health checks & monitoring)
- **PostgreSQL 16**
- **H2 Database** (development)
- **Docker & Docker Compose**
- **Lombok**
- **Maven**
- **Swagger/OpenAPI 3** (SpringDoc)
- **JUnit 5**
- **Mockito**
- **JaCoCo** (code coverage)
- **Coveralls** (coverage reporting)

## CI/CD - CircleCI Integration

The project includes CircleCI configuration for automated builds and testing.

### Configuration Files

- **`.circleci/config.yml`** - Full CI/CD pipeline with multiple jobs
- **`.circleci/config-simple.yml`** - Simplified single-job configuration

### Pipeline Stages

1. **Build** - Compiles the application
2. **Test** - Runs all tests (unit, integration, E2E)
3. **Verify** - Validates code coverage thresholds
4. **Package** - Creates JAR artifact
5. **Security Scan** - Optional security checks

### Setup Instructions

1. **Connect Repository to CircleCI:**
   - Go to [CircleCI](https://circleci.com/)
   - Sign in with GitHub/Bitbucket
   - Add your project
   - CircleCI will automatically detect `.circleci/config.yml`

2. **Configure Coveralls for Coverage Reports:**
   - Go to [Coveralls.io](https://coveralls.io/)
   - Sign in with GitHub/Bitbucket
   - Add your repository
   - Copy the repository token
   - In CircleCI, go to Project Settings → Environment Variables
   - Add `COVERALLS_REPO_TOKEN` with the token from Coveralls

3. **Optional: Configure Docker Hub Credentials** (to avoid rate limits)
   - Go to Project Settings → Environment Variables
   - Add `DOCKERHUB_USERNAME`
   - Add `DOCKERHUB_PASSWORD`
   - Create a context named `docker-hub-creds`

4. **View Build Results:**
   - Test results available in CircleCI UI
   - Coverage reports stored as artifacts
   - Coverage trends available on Coveralls.io
   - JAR files available for download

### CircleCI Features

- ✅ **Caching** - Maven dependencies cached for faster builds
- ✅ **Test Results** - JUnit reports displayed in UI
- ✅ **Coverage Reports** - JaCoCo reports as artifacts
- ✅ **Coveralls Integration** - Automatic coverage reporting to Coveralls.io
- ✅ **Artifacts** - JAR files and reports stored
- ✅ **Workflows** - Parallel job execution
- ✅ **Scheduled Builds** - Nightly builds on main branch

### Build Status Badge

Add this to show build status:
```markdown
[![CircleCI](https://circleci.com/gh/YOUR_USERNAME/YOUR_REPO.svg?style=svg)](https://circleci.com/gh/YOUR_USERNAME/YOUR_REPO)
```

### Local Testing

Test CircleCI configuration locally:
```bash
# Install CircleCI CLI
curl -fLSs https://raw.githubusercontent.com/CircleCI-Public/circleci-cli/master/install.sh | bash

# Validate configuration
circleci config validate

# Run job locally (requires Docker)
circleci local execute --job build-and-test
```

## Development Notes

- The domain layer has zero Spring/JPA dependencies
- All framework-specific code is isolated in the infrastructure layer
- Use cases are defined as interfaces in the domain layer
- Repository adapters handle the mapping between domain models and JPA entities
- DTOs are used for API communication to maintain clean boundaries
