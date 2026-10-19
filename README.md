# TuniRide

Taxi booking platform for Tunisia: riders request a ride, nearby licensed taxi drivers accept it,
both follow the trip in real time, and payment is cash first (local mobile payments later).

Built as a learning project to practice a production-grade Java stack end to end.

## Stack

| Layer | Tech |
|---|---|
| Backend | Java 21, Spring Boot 4, Spring Data JPA, Flyway |
| Data | PostgreSQL 17 + PostGIS, Redis |
| Tests | JUnit 5, Testcontainers |
| Clients (planned) | Flutter (rider & driver apps), Angular (admin) |
| DevOps | Docker, GitHub Actions, AWS (planned) |

## Run locally

Requirements: JDK 21, Docker.

```bash
docker compose -f infra/docker-compose.yml up -d
cd backend && ./mvnw spring-boot:run
```

- Health: http://localhost:8080/actuator/health
- API docs: http://localhost:8080/swagger-ui.html

## Tests

```bash
cd backend && ./mvnw verify
```

## Roadmap

See [docs/ROADMAP.md](docs/ROADMAP.md).
