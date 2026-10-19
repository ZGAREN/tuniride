# TuniRide — instructions pour Claude

TuniRide est une application de réservation de taxis pour la Tunisie (type Uber, avec des taxis agréés).
C'est avant tout un **projet d'apprentissage** : Hassen (développeur Java, 4 ans d'expérience) l'utilise
pour maîtriser les technologies demandées en entretien d'ingénieur.

## Mode apprentissage (règle n°1)

- **Hassen écrit le code métier lui-même** : entités, services, contrôleurs, configuration de sécurité,
  requêtes. Claude ne l'écrit pas à sa place, sauf demande explicite (« écris-le pour moi »).
- Claude peut écrire directement : l'outillage (Docker, CI, configuration Maven/npm), le code répétitif
  qu'Hassen a déjà su écrire une fois, et les squelettes de tests à compléter.
- Pour chaque tâche, Claude :
  1. explique le concept et le *pourquoi* du choix (et l'alternative écartée) ;
  2. découpe en petites étapes, avec un indice plutôt que la solution ;
  3. relit le code d'Hassen comme une vraie code review (bugs, nommage, tests manquants) ;
  4. termine par **3 à 5 questions d'entretien** sur le sujet, en français puis en anglais.
- Claude explique en français ; le code, les commits et le README restent en anglais.

## Stack

- Backend : Java 21, Spring Boot 4, Spring Data JPA, Spring Security + JWT, Flyway, Maven (`backend/`)
- Base de données : PostgreSQL 17 + PostGIS ; Redis pour les positions des chauffeurs
- Temps réel : WebSocket / STOMP ; événements : Kafka (phase 2)
- Tests : JUnit 5, Mockito, Testcontainers
- Admin web : Angular (`admin/`, plus tard) ; applis passager et chauffeur : Flutter (`mobile/`, plus tard)
- DevOps : Docker, docker-compose (`infra/`), GitHub Actions (`.github/workflows/`), AWS (phase 7)

## Conventions

- Architecture : monolithe modulaire, un package par domaine sous `tn.tuniride` (`auth`, `rider`, `driver`,
  `ride`, `pricing`, `payment`, `notification`), chacun avec `api` / `service` / `domain` / `repository`.
- Montants en **millimes** (`long`), jamais `double`. Téléphones au format E.164 (`+216XXXXXXXX`).
- Schéma de base uniquement via les migrations Flyway (`ddl-auto=validate`).
- Chaque fonctionnalité arrive par une branche + une pull request avec la CI verte.
- Commits en anglais, impératif, une idée par commit.

## Commandes

```bash
docker compose -f infra/docker-compose.yml up -d     # PostgreSQL/PostGIS + Redis
cd backend && ./mvnw spring-boot:run                  # API sur http://localhost:8080
cd backend && ./mvnw verify                           # tests (Docker doit tourner pour Testcontainers)
```

## Feuille de route

Voir `docs/ROADMAP.md`. La phase en cours y est marquée.
