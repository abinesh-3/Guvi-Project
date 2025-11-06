# Use a stable Java 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory inside container
WORKDIR /app

# Copy built JAR file into the container
COPY target/*.jar app.jar

# Expose port 8080 for Render
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
