# StudyPlanner API

API Spring Boot pour gérer des sessions de révision, avec sécurité Basic Auth, base H2 en mémoire, documentation OpenAPI, et endpoints Actuator.

## Prérequis

- Java 21
- Maven (ou wrapper Maven inclus)

## Lancer l'application
                                                           
```bash
./mvnw spring-boot:run
```


Application disponible sur:

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Utilisateurs de test (Basic Auth)

Utilisez l'authentification HTTP Basic sur les routes API (/api/**):

- alice / alice123
- bob / bob123
- charlie / charlie123

Exemple d'entête Basic Auth en curl:

```bash
-u alice:alice123
```

## Endpoints principaux

Base URL: http://localhost:8080/api/sessions

- POST /api/sessions
- GET /api/sessions
- GET /api/sessions/{id}
- PUT /api/sessions/{id}
- DELETE /api/sessions/{id}

## Exemples curl avec réponses attendues

### 1) Créer une session

```bash
curl -i -X POST "http://localhost:8080/api/sessions" \
  -u alice:alice123 \
  -H "Content-Type: application/json" \
  -d '{
    "subject": "Maths",
    "description": "Réviser les intégrales",
    "startTime": "2030-04-08T18:00:00",
    "endTime": "2030-04-08T19:30:00"
  }'
```

Réponse attendue (201 Created):

```json
{
  "id": 1,
  "studentName": "alice",
  "subject": "Maths",
  "description": "Réviser les intégrales",
  "startTime": "2030-04-08T18:00:00",
  "endTime": "2030-04-08T19:30:00",
  "createdAt": "2030-04-07T10:00:00.123",
  "updatedAt": "2030-04-07T10:00:00.123"
}
```

Notes:

- studentName est forcé avec l'utilisateur authentifié (ici alice).
- startTime doit être dans le futur.
- endTime doit être strictement après startTime.

### 2) Lister les sessions de l'utilisateur connecté

```bash
curl -i -X GET "http://localhost:8080/api/sessions" \
  -u alice:alice123
```

Réponse attendue (200 OK):

```json
[
  {
    "id": 1,
    "studentName": "alice",
    "subject": "Maths",
    "description": "Réviser les intégrales",
    "startTime": "2030-04-08T18:00:00",
    "endTime": "2030-04-08T19:30:00",
    "createdAt": "2030-04-07T10:00:00.123",
    "updatedAt": "2030-04-07T10:00:00.123"
  }
]
```

### 3) Récupérer une session par id

```bash
curl -i -X GET "http://localhost:8080/api/sessions/1" \
  -u alice:alice123
```

Réponse attendue (200 OK):

```json
{
  "id": 1,
  "studentName": "alice",
  "subject": "Maths",
  "description": "Réviser les intégrales",
  "startTime": "2030-04-08T18:00:00",
  "endTime": "2030-04-08T19:30:00",
  "createdAt": "2030-04-07T10:00:00.123",
  "updatedAt": "2030-04-07T10:00:00.123"
}
```

### 4) Mettre à jour une session

```bash
curl -i -X PUT "http://localhost:8080/api/sessions/1" \
  -u alice:alice123 \
  -H "Content-Type: application/json" \
  -d '{
    "subject": "Physique",
    "description": "Réviser la mécanique",
    "studentName": "alice",
    "startTime": "2030-04-08T20:00:00",
    "endTime": "2030-04-08T21:00:00"
  }'
```

Réponse attendue (200 OK):

```json
{
  "id": 1,
  "studentName": "alice",
  "subject": "Physique",
  "description": "Réviser la mécanique",
  "startTime": "2030-04-08T20:00:00",
  "endTime": "2030-04-08T21:00:00",
  "createdAt": "2030-04-07T10:00:00.123",
  "updatedAt": "2030-04-07T10:05:00.456"
}
```

### 5) Supprimer une session

```bash
curl -i -X DELETE "http://localhost:8080/api/sessions/1" \
  -u alice:alice123
```

Réponse attendue: 204 No Content (corps vide).

### 6) Exemple d'erreur de validation (400)

Cas: startTime dans le passé.

```bash
curl -i -X POST "http://localhost:8080/api/sessions" \
  -u alice:alice123 \
  -H "Content-Type: application/json" \
  -d '{
    "subject": "Maths",
    "description": "Session invalide",
    "startTime": "2020-04-08T18:00:00",
    "endTime": "2030-04-08T19:00:00"
  }'
```

Réponse attendue (400 Bad Request):

```json
{
  "code": "BAD_REQUEST",
  "message": "Erreurs de validation",
  "erreurs": {
    "startTime": "La date de début doit être dans le futur"
  },
  "timestamp": "2030-04-07T10:15:00.000"
}
```

### 7) Exemple d'erreur métier (422)

Cas: endTime <= startTime.

```bash
curl -i -X POST "http://localhost:8080/api/sessions" \
  -u alice:alice123 \
  -H "Content-Type: application/json" \
  -d '{
    "subject": "Chimie",
    "description": "Dates incohérentes",
    "startTime": "2030-04-08T19:00:00",
    "endTime": "2030-04-08T18:00:00"
  }'
```

Réponse attendue (422 Unprocessable Entity):

```json
{
  "code": "UNPROCESSABLE_ENTITY",
  "message": "La date de fin doit être après la date de début",
  "erreurs": {},
  "timestamp": "2030-04-07T10:20:00.000"
}
```

### 8) Exemple d'erreur d'authentification (401)

```bash
curl -i -X GET "http://localhost:8080/api/sessions"
```

Réponse attendue: 401 Unauthorized.

## Accès H2 Console

La console H2 est activée et accessible sans authentification API:

- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:studyplannerdb
- User Name: sa
- Password: (laisser vide)

Important:

- La base est en mémoire, les données sont perdues à l'arrêt de l'application.

## Accès Actuator

L'endpoint suivant est exposé:

- http://localhost:8080/actuator/health

Exemple:

```bash
curl -i "http://localhost:8080/actuator/health"
```

Réponse attendue (exemple):

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    },
    "studyLoadHealthIndicator": {
      "status": "UP",
      "details": {
        "totalSessions": 1,
        "status": "OK"
      }
    }
  }
}
```

Remarque: le statut de studyLoadHealthIndicator passe en DOWN si le total de sessions dépasse 50.

## Exécuter les tests

```bash
./mvnw test
```
