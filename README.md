# spring-boot-mongo-db-service

**Project Overview:**
This project is a **full-featured CRUD application** built with **Java 21** and **Spring Boot 3.5.5**, designed to
manage user data stored in **MongoDB**. The application follows modern software development best practices and is fully
containerized, enabling seamless deployment and scaling across multiple environments.

**Key Features:**

- **CRUD Operations:** Create, Read, Update, and Delete user records via RESTful APIs.
- **Spring Boot & Java 21:** Leveraging the latest features of Java 21 and Spring Boot 3.5.5 for performance,
  maintainability, and modern development practices.
- **MongoDB Integration:** Uses MongoDB as the primary data store, with environment-based configuration for
  flexibility (e.g., dev, prod, Kubernetes).
- **Containerized Deployment:**
    - **Docker:** Easily build and run the application as a containerized service.
    - **Docker Compose:** Supports multi-container setups for local development (Spring Boot + MongoDB + Mongo Express).
    - **Kubernetes:** Fully Kubernetes-ready with deployments, services, persistent volume claims, and
      readiness/liveness probes for production-grade orchestration.
- **Resilience & Observability:** Includes Spring Boot Actuator endpoints for monitoring and health checks, supporting
  readiness and liveness probes in Kubernetes.
- **Scalability:** Supports horizontal scaling in Kubernetes with replica sets.
- **Configuration Management:** Environment variables are used for database credentials and application profiles,
  enabling flexible deployments across different environments.
- **Resource Management:** Configured CPU and memory requests/limits for efficient container orchestration.
- **Persistent Storage:** Uses PersistentVolumeClaims (PVCs) in Kubernetes to ensure MongoDB data durability.

---

**Spring Boot + MongoDB** CRUD service built with **Java 21**, **Spring Boot 3.5.5**, **Gradle**, and containerized for
**Docker**, **Docker Compose**, and **Kubernetes**.

---

## 📑 Table of Contents

1. [✅ Tech Stack](#-tech-stack)
2. [📦 Project Structure](#-project-structure)
3. [🚀 Run Locally](#-run-locally)
    - [1) Prerequisites](#1-prerequisites)
    - [2) Run with Docker Compose](#2-run-with-docker-compose)
    - [3) Stop / Take Down](#3-stop--take-down)
4. [🧰 Build & Run (without Docker)](#-build--run-without-docker)
5. [🐳 Docker](#-docker)
6. [☸️ Kubernetes (with Minikube)](#-kubernetes-with-minikube)
    - [Stop / Clean up](#stop--clean-up)
7. [📚 API (Users)](#-api-users)
8. [📖 API Documentation](#-api-documentation)
9. [🔧 Configuration](#-configuration)
10. [🖼️ Application Screenshots](#-application-screenshots)
11. [🧪 Tests](#-tests)
12. [📄 License](#-license)

---

## ✅ Tech Stack

* **Java**: 21 (LTS)
* **Spring Boot**: 3.5.5
* **Spring Data MongoDB**
* **Build**: Gradle (Kotlin DSL)
* **Database**: MongoDB
* **Container**: Dockerfile + Docker Compose
* **K8s**: Namespaced manifests with probes and ConfigMap/Secret
* **API Docs**: OpenAPI 3 + Swagger UI
* **Mongo GUI**: Mongo Express

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
│ ├── mongo-deployment.yaml
│ ├── mongo-express-deployment.yaml
│ ├── spring-boot-deployment.yaml
├── src/
│ ├── main/java/com/sid/app/
│ │ ├── SpringBootMongoDbServiceApplication.java
│ │ ├── constant/AppConstants.java
│ │ ├── entity/User.java
│ │ ├── repository/UserRepository.java
│ │ ├── service/UserService.java
│ │ ├── controller/UserController.java
│ │ └── model/ApiResponse.java
│ └── resources/application.yml
└── README.md

````

---

## 🚀 Run Locally

### 1) Prerequisites

* JDK 21
* Gradle 8.x (`./gradlew`)
* Docker Desktop / Docker Engine
* (Optional) Minikube or kind for Kubernetes

---

### 2) Run with Docker Compose

```bash
docker compose up --build
````

* Spring Boot API: [http://localhost:8080](http://localhost:8080)
* Mongo Express GUI: [http://localhost:8081](http://localhost:8081) (Basic Auth: `admin/admin`)
* Health endpoint: `/actuator/health`
* Users API base path: `/api/v1/spring-boot-mongo-db-service/users`

#### Sample API requests

```bash
# Create user
curl -X POST http://localhost:8080/api/v1/spring-boot-mongo-db-service/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Siddhant Patni",
    "email": "siddhant@gmail.com",
    "phone": "+91-9090909090",
    "role": "ADMIN",
    "status": "ACTIVE",
    "address": "Pune"
}'

# Get all users
curl http://localhost:8080/api/v1/spring-boot-mongo-db-service/users

# Get user by ID
curl http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1

# Update user
curl -X PUT http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Siddhant Patni",
    "email": "siddhant@gmail.com",
    "phone": "+91-8080808080",
    "role": "ADMIN",
    "status": "ACTIVE",
    "address": "Pune"
}'

# Delete user
curl -X DELETE http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1
```

---

### 3) Stop / Take Down

#### Docker Compose

```bash
docker compose down        # stops containers
docker compose down -v     # stops containers and removes volumes
```

#### Spring Boot Jar

```bash
pkill -f spring-boot-mongo-db-service
```

#### Kubernetes / Minikube

```bash
kubectl delete -f k8s/spring-boot-deployment.yaml
kubectl delete -f k8s/mongo-deployment.yaml
kubectl delete -f k8s/mongo-express-deployment.yaml

# OR delete entire namespace if used
kubectl delete namespace mongo-demo

# Stop Minikube
minikube stop
minikube delete
```

---

## 🧰 Build & Run (without Docker)

```bash
# Build the jar
./gradlew clean bootJar

# Run (requires local MongoDB)
java -jar build/libs/spring-boot-mongo-db-service.jar
```

Or specify your own Mongo URI:

```bash
SPRING_DATA_MONGODB_URI='mongodb://user:pwd@host:27017/users_db?authSource=admin' \
java -jar build/libs/spring-boot-mongo-db-service.jar
```

---

## 🐳 Docker

### 1) Build & Run

```bash
./gradlew clean bootJar
docker build -t spring-boot-mongo-db-service:latest .
docker run --rm -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI='mongodb://localhost:27017/users_db' \
  spring-boot-mongo-db-service:latest
```

### 2) Compose (App + MongoDB + Mongo Express)

```bash
docker compose up --build
```

---

## ☸️ Kubernetes (with Minikube)

### Step-by-step instructions

1. **Start Minikube**

```bash
minikube start --memory=4096 --cpus=2
````

2. **Enable Dashboard (optional)**

```bash
minikube dashboard
```

3. **Use Minikube Docker daemon**

```bash
eval $(minikube docker-env)
```

4. **Build Spring Boot image inside Minikube**

```bash
./gradlew clean bootJar
docker build -t spring-boot-mongo-db-service:latest .
```

5. **Apply Kubernetes manifests**

```bash
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-express-deployment.yaml
kubectl apply -f k8s/spring-boot-mongo-db-deployment.yaml
```

6. **Check pods and status**

```bash
kubectl get pods
kubectl describe pod <pod-name>
```

7. **Access services locally**

```bash
# Option 1: Port-forward (temporary)
kubectl port-forward svc/spring-boot-mongo-db-service 8080:8080
kubectl port-forward svc/mongo-express 8081:8081

# Option 2: Get Minikube service URL (persistent)
minikube service spring-boot-mongo-db-service --url
minikube service mongo-express --url
```

8. **Verify service ports**

```bash
kubectl get svc
```

9. **Stop / Clean up**

```bash
# Delete Kubernetes resources
kubectl delete -f k8s/spring-boot-mongo-db-deployment.yaml
kubectl delete -f k8s/mongo-deployment.yaml
kubectl delete -f k8s/mongo-express-deployment.yaml

# Stop and delete Minikube
minikube stop
minikube delete

# Delete all images inside Minikube's Docker environment
eval $(minikube -p minikube docker-env)  # point Docker CLI to Minikube's Docker
docker images -q | xargs -r docker rmi -f

```

---

## 📚 API (Users)

| Method | Path                                              | Description    |
|-------:|---------------------------------------------------|----------------|
|    GET | `/api/v1/spring-boot-mongo-db-service/users`      | List all users |
|    GET | `/api/v1/spring-boot-mongo-db-service/users/{id}` | Get user by ID |
|   POST | `/api/v1/spring-boot-mongo-db-service/users`      | Create user    |
|    PUT | `/api/v1/spring-boot-mongo-db-service/users/{id}` | Update user    |
| DELETE | `/api/v1/spring-boot-mongo-db-service/users/{id}` | Delete user    |

---

## 📖 API Documentation

* Swagger UI → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* OpenAPI JSON → [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🔧 Configuration

* `SPRING_DATA_MONGODB_URI` → MongoDB URI
* `SERVER_PORT` → port (default `8080`)
* Profiles:

    * `default` → local dev
    * `docker` → Docker Compose
    * `k8s` → Kubernetes

---

## 🖼️ Application Screenshots

### Kubernetes Dashboard

<img src="src/main/resources/artifacts/kubernetes-dashboard-1.png" width="700"/>

### Running Pods

<img src="src/main/resources/artifacts/kubernetes-pods-1.png" width="700"/>

### Mongo Express UI

<img src="src/main/resources/artifacts/mongo-express-1.png" width="700"/>

### Mongo Express UI (Another View)

<img src="src/main/resources/artifacts/mongo-express-2.png" width="700"/>

---

## 🧪 Tests

```bash
./gradlew test
```

---

## 📄 License

Apache-2.0

---