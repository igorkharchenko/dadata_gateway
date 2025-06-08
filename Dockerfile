# Stage 1: Build Stage
FROM gradle:jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build

# Stage 2: Runtime Stage
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar"]