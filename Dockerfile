# Start with a base image that includes Java
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files, including the Maven Wrapper
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Make the Maven Wrapper script executable
RUN chmod +x mvnw

# Use Maven to build the project. We skip tests for a faster build.
RUN ./mvnw clean install -DskipTests

# Expose the port that your Spring Boot app runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "target/CBLOS-0.0.1-SNAPSHOT.jar"]
