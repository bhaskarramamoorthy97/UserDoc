# Stage 1: Build the JAR with Maven and Java 17
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the app using Java 21
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /workspace/target/my-first-app.jar my-first-app.jar
EXPOSE 8080
CMD ["java", "-jar", "my-first-app.jar"]
