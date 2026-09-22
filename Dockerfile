# ---- Build stage ----
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app
COPY backend/pom.xml .
# Cache dependencies separately for faster rebuilds
RUN mvn -B dependency:go-offline
COPY backend/src ./src
RUN mvn -B clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Non-root user for better container security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Configurable temp/output directory instead of hardcoded paths
ENV APP_STORAGE_DIR=/tmp/arb-converter
RUN mkdir -p ${APP_STORAGE_DIR}

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
