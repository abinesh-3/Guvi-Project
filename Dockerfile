# Use stable JDK 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven files first (for dependency caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY . .

# Build the JAR inside Docker
RUN ./mvnw -B -DskipTests clean package

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "target/*.jar"]
