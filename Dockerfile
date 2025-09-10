# Use Java 17 JDK slim image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the jar file from target folder to container
COPY target/my-first-app.jar my-first-app.jar

# Expose port your app runs on
EXPOSE 8080

# Command to run the jar
CMD ["java", "-jar", "my-first-app.jar"]

