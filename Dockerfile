# Use a lightweight Java 21 runtime
FROM eclipse-temurin:21-jre-alpine

# Set a non-root user for better security
RUN addgroup -S app && adduser -S app -G app
USER app

# Copy the fat jar built via Gradle
ARG JAR_FILE=build/libs/spring-boot-mongo-db-service.jar
WORKDIR /home/app
COPY --chown=app:app ${JAR_FILE} app.jar

# Healthcheck (optional) - relies on actuator
HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# Allow runtime flags via JAVA_OPTS
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /home/app/app.jar" ]
