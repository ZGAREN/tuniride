# Feuille de route TuniRide

Chaque phase = une branche, une ou plusieurs pull requests, la CI verte, puis les questions d'entretien.

| # | Phase | Ce que tu construis | Ce que tu maîtrises pour l'entretien | Statut |
|---|---|---|---|---|
| 0 | Fondations | Monorepo, docker-compose (PostGIS, Redis), Spring Boot 4 / Java 21, Flyway, Testcontainers, CI | Docker, Git/PR, CI, pourquoi Testcontainers plutôt que H2 | ✅ fait |
| 1 | Premier domaine | Entité `Driver` + migration, repository, service, contrôleur REST, gestion d'erreurs, tests | Couches, DTO vs entité, `@Transactional`, validation, `@ControllerAdvice` | ✅ fait |
| 2 | Authentification | Connexion par téléphone + code SMS (simulé), JWT access + refresh, rôles RIDER/DRIVER/ADMIN | Spring Security, filtres, stateless, rotation de refresh token | 👉 prochaine |
| 3 | Courses | Demande de course, machine à états (REQUESTED → ACCEPTED → STARTED → COMPLETED / CANCELLED), prix en millimes | Modélisation du domaine, enum + transitions, `long` vs `BigDecimal` | |
| 4 | Matching | Positions chauffeurs dans Redis GEO, chauffeurs à moins de 3 km, acceptation concurrente | Redis, PostGIS, verrou optimiste `@Version`, conditions de course | |
| 5 | Temps réel + clients | WebSocket/STOMP pour suivre le taxi ; appli Flutter passager/chauffeur ; admin Angular | WebSocket vs polling, RxJS, état côté client | |
| 6 | Qualité | Couverture de tests, tests d'API, analyse statique, Dockerfile multi-étapes, image publiée | Pyramide des tests, images Docker légères | |
| 7 | AWS | Alerte budget, EC2 + docker-compose, puis RDS, S3 + CloudFront, ECR + ECS ; CD depuis GitHub Actions | IAM, VPC, security groups, coûts | |
| 8 | Microservices | Kafka (événements de course), extraction des services `notification` et `ride`, API Gateway, Resilience4j, observabilité | Événements, cohérence à terme, résilience, traçage | |

## Exercice en cours — Phase 1 : le domaine `driver`

But : un CRUD propre, testé, comme en entreprise. **C'est toi qui codes** ; demande à Claude des indices ou une relecture.

1. Branche `feature/driver-crud`.
2. Migration `V2__create_driver.sql` : table `driver` (`id` UUID, `full_name`, `phone` unique au format `+216XXXXXXXX`,
   `taxi_license_number` unique, `status` parmi `OFFLINE`/`AVAILABLE`/`ON_RIDE`, `created_at`).
3. Package `tn.tuniride.driver` avec `domain` (entité), `repository`, `service`, `api` (contrôleur + DTO en `record`).
4. Endpoints : `POST /api/drivers`, `GET /api/drivers/{id}`, `GET /api/drivers?status=AVAILABLE`, `PATCH /api/drivers/{id}/status`.
5. Erreurs : 404 si introuvable, 409 si téléphone déjà utilisé, 400 si validation KO, au format `ProblemDetail`.
6. Tests : un test unitaire du service (Mockito) et un test d'intégration du contrôleur (Testcontainers).
7. Ouvre une pull request et vérifie que la CI passe.

Questions à préparer pour cette phase :
- Pourquoi ne pas exposer l'entité JPA directement dans l'API ?
- Que fait `@Transactional` et pourquoi `open-in-view: false` ?
- Différence entre `PUT` et `PATCH` ? Quel code HTTP pour une création ?
- Pourquoi un UUID plutôt qu'un identifiant auto-incrémenté ?
- Comment Flyway sait-il quelles migrations appliquer ?
