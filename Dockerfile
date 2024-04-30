# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean install

# Package stage
FROM eclipse-temurin:21-jdk
COPY --from=build /target/arb-excel-converter-web-0.0.1-SNAPSHOT.jar arb-excel-converter-web-0.0.1-SNAPSHOT.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "arb-excel-converter-web-0.0.1-SNAPSHOT.jar"]