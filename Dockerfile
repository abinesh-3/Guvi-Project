# Use stable JDK 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper files first (for dependency caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (cached)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY . .

# Build the JAR inside Docker
RUN ./mvnw -B -DskipTests clean package

# Expose the application port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "target/*.jar"]
