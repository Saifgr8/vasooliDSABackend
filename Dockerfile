# Stage 1: Build the Java application
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
# Copy src/ to prevent issues with changing files and rebuilds
COPY src ./src
RUN mvn clean package -DskipTests  # This command builds your JAR!

# Stage 2: Create the final lean image
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]