# spring-boot-mongo-db-service

A production-ready **Spring Boot + MongoDB** CRUD service built with **Java 21**, **Spring Boot 3.5.5**, **Gradle**, and
containerized for **Docker**, **Docker Compose**, and **Kubernetes**.

> TL;DR: Run it locally with Mongo in Docker using one command:
>
> ```bash
> cp .env.example .env && docker compose up --build
> ```

---

## ✅ Tech Stack

* **Java**: 21 (LTS)
* **Spring Boot**: 3.5.5
* **Spring Data MongoDB**: managed by Spring Boot BOM
* **Build**: Gradle (Kotlin DSL)
* **Database**: MongoDB (with auto-increment ID)
* **Container**: Dockerfile + docker-compose
* **K8s**: Namespaced manifests with probes and ConfigMap/Secret
* **API Docs**: OpenAPI 3 + Swagger UI

---

## 📦 Project Structure

````

spring-boot-mongo-db-service
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── k8s/
│   ├── 00-namespace.yaml
│   ├── 10-configmap.yaml
│   ├── 11-secret.yaml
│   ├── 20-mongo.yaml
│   └── 30-app.yaml
├── src/
│   ├── main/java/com/sid/app/
│   │   ├── SpringBootMongoDbServiceApplication.java
│   │   ├── constant/AppConstants.java
│   │   ├── entity/User.java
│   │   ├── repository/UserRepository.java
│   │   ├── service/UserService.java
│   │   ├── controller/UserController.java
│   │   └── model/ApiResponse.java
│   └── resources/application.yml
└── README.md

````

---

## 🚀 Run Locally

### 1) Prereqs

* JDK 21
* Gradle 8.x (or use the wrapper `./gradlew`)
* Docker Desktop or Docker Engine
* (Optional) **minikube** or **kind** for K8s

### 2) Start with Docker Compose

```bash
cp .env.example .env
docker compose up --build
````

* App: [http://localhost:8080](http://localhost:8080)
* Health: `GET /actuator/health`
* API base: `/api/v1/spring-boot-mongo-db-service/users`

#### Sample requests

```bash
curl -X POST http://localhost:8080/api/v1/spring-boot-mongo-db-service/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Ada Lovelace","email":"ada@example.com"}'

curl http://localhost:8080/api/v1/spring-boot-mongo-db-service/users
curl http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1
curl -X PUT http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ada L.","email":"ada@example.com"}'
curl -X DELETE http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1
```

---

## 🧰 Build & Run (without Docker)

```bash
# Build
./gradlew clean bootJar

# Run (requires local MongoDB running at 27017)
java -jar build/libs/spring-boot-mongo-db-service.jar
```

Or provide your own URI:

```bash
SPRING_DATA_MONGODB_URI='mongodb://user:pwd@host:27017/users_db?authSource=admin' \
java -jar build/libs/spring-boot-mongo-db-service.jar
```

---

## 🐳 Docker

### Build and Run

```bash
./gradlew clean bootJar
docker build -t spring-boot-mongo-db-service:latest .
docker run --rm -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI='mongodb://localhost:27017/users_db' \
  spring-boot-mongo-db-service:latest
```

### Compose (App + Mongo)

```bash
docker compose up --build
```

---

## ☸️ Kubernetes (with Minikube)

1. Start **minikube**:

   ```bash
   minikube start --memory=4096 --cpus=2
   ```

2. Build and load the image into minikube:

   ```bash
   ./gradlew clean bootJar
   eval $(minikube docker-env)
   docker build -t spring-boot-mongo-db-service:latest .
   ```

3. Apply manifests:

   ```bash
   kubectl apply -f k8s/00-namespace.yaml
   kubectl apply -f k8s/10-configmap.yaml -f k8s/11-secret.yaml
   kubectl apply -f k8s/20-mongo.yaml
   kubectl apply -f k8s/30-app.yaml
   ```

4. Forward ports to access locally:

   ```bash
   kubectl -n mongo-demo port-forward svc/spring-boot-mongo-db-service 8080:8080
   ```

5. Test:

   ```bash
   curl http://localhost:8080/actuator/health
   curl http://localhost:8080/api/v1/spring-boot-mongo-db-service/users
   ```

---

## 📚 API (Users)

### Endpoints

| Method | Path                                              | Description    |
| -----: | ------------------------------------------------- | -------------- |
|    GET | `/api/v1/spring-boot-mongo-db-service/users`      | List all users |
|    GET | `/api/v1/spring-boot-mongo-db-service/users/{id}` | Get by id      |
|   POST | `/api/v1/spring-boot-mongo-db-service/users`      | Create         |
|    PUT | `/api/v1/spring-boot-mongo-db-service/users/{id}` | Update         |
| DELETE | `/api/v1/spring-boot-mongo-db-service/users/{id}` | Delete         |

---

## 📦 API Response Format

All responses are wrapped in a standard `ApiResponse<T>`:

```json
{
  "statusCode": 200,
  "status": "SUCCESS",
  "message": "Users retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Ada Lovelace",
      "email": "ada@example.com",
      "role": "USER",
      "status": "ACTIVE"
    }
  ]
}
```

### Fields

* **statusCode** → HTTP code (200, 201, 404, etc.)
* **status** → `SUCCESS` or `ERROR`
* **message** → human-readable status message (from constants)
* **data** → response body (object, list, or null)

---

## 📖 API Documentation (Swagger / OpenAPI)

Once the app is running:

* Swagger UI → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* OpenAPI JSON → [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

This makes it easy to explore and test APIs directly in the browser.

---

## 🧪 Tests

Run service-layer tests:

```bash
./gradlew test
```

---

## 🔧 Config

Configurable via `application.yml` or environment vars:

* `SPRING_DATA_MONGODB_URI` → full Mongo URI
* `SERVER_PORT` → port (default `8080`)
* Profiles:

    * `default` → local dev
    * `docker` → docker-compose
    * `k8s` → Kubernetes

---

## 📄 License

Apache-2.0 (or your org’s standard license).

---
