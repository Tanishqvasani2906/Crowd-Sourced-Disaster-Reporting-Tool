# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application's jar file to the container
COPY target/Disaster-Management-Tool-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
CMD ["java", "-jar", "app.jar"]
