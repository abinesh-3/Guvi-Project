# Use Java 21 base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy built JAR from target folder
COPY target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
