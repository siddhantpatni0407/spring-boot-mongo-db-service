Here’s an **enhanced README** with **detailed, step-by-step instructions** to run the app in both Docker and Kubernetes
environments, including Mongo Express and more clarity for each step:

---

# spring-boot-mongo-db-service

A production-ready **Spring Boot + MongoDB** CRUD service built with **Java 21**, **Spring Boot 3.5.5**, **Gradle**, and
containerized for **Docker**, **Docker Compose**, and **Kubernetes**.

> TL;DR: Run it locally with MongoDB + Mongo Express using one command:
>
> ```bash
> docker compose up --build
> ```

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

```
spring-boot-mongo-db-service
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── k8s/
│   ├── mongo-deployment.yaml
│   ├── mongo-express-deployment.yaml
│   ├── spring-boot-deployment.yaml
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
```

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
```

* Spring Boot API: [http://localhost:8080](http://localhost:8080)
* Mongo Express GUI: [http://localhost:8081](http://localhost:8081) (Basic Auth: `admin/admin123`)
* Health endpoint: `/actuator/health`
* Users API base path: `/api/v1/spring-boot-mongo-db-service/users`

#### Sample API requests

```bash
# Create user
curl -X POST http://localhost:8080/api/v1/spring-boot-mongo-db-service/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Ada Lovelace","email":"ada@example.com"}'

# Get all users
curl http://localhost:8080/api/v1/spring-boot-mongo-db-service/users

# Get user by ID
curl http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1

# Update user
curl -X PUT http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ada L.","email":"ada@example.com"}'

# Delete user
curl -X DELETE http://localhost:8080/api/v1/spring-boot-mongo-db-service/users/1
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
```

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
kubectl apply -f k8s/spring-boot-deployment.yaml
```

6. **Check pods and status**

```bash
kubectl get pods
kubectl describe pod <pod-name>
```

7. **Access services locally**

```bash
# Spring Boot API
kubectl port-forward svc/spring-boot-mongo-db-service 8080:8080

# Mongo Express GUI
kubectl port-forward svc/mongo-express 8081:8081
```

8. **Verify service ports**

```bash
kubectl get svc
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

## 🧪 Tests

```bash
./gradlew test
```

---

Perfect! I can update your README to include this screenshot. Here’s an enhanced section you can add under a *
*Screenshots / Application Demo** section:

---

## 🖼️ Application Screenshots

### Kubernetes Dashboard
<img src="src/main/resources/artifacts/kubernetes-dashboard-1.png" width="500"/>

### Running Pods
<img src="src/main/resources/artifacts/kubernetes-pods-1.png" width="500"/>

### Mongo Express UI
<img src="src/main/resources/artifacts/mongo-express-1.png" width="500"/>

### Mongo Express UI (Another View)
<img src="src/main/resources/artifacts/mongo-express-2.png" width="500"/>

---

## 📄 License

Apache-2.0

---

This README now includes **Mongo Express**, detailed Docker and Kubernetes steps, health checks, port-forwarding, and
one-by-one instructions for developers to run and test the service.

If you want, I can also add a **“one-command Minikube deploy script”** that builds images, applies all manifests, and
opens the dashboard automatically. Do you want me to add that?
