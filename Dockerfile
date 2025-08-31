# ------------------------
# Base image: lightweight Java 21 runtime
# ------------------------
FROM eclipse-temurin:21-jre-alpine AS builder

# Set a non-root user for security
RUN addgroup -S app && adduser -S app -G app
USER app

# Set working directory
WORKDIR /app

# Copy the fat jar built via Gradle
ARG JAR_FILE=build/libs/spring-boot-mongo-db-service.jar
COPY ${JAR_FILE} app.jar

# Expose application port
EXPOSE 8080

# Use non-root user
USER app

# Run the application with a default Spring profile
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]

# ------------------------
# Optional: Health check (helps Kubernetes detect container health)
# ------------------------
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
