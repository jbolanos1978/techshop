# Use a base image with Java installed
FROM openjdk:17-jdk-alpine

# Expose the port your Spring Boot application listens on (default is 8080)
EXPOSE 80

# Define an argument for the JAR file name (can be passed during build)
ARG JAR_FILE=target/*.jar

# Copy the built JAR file into the container
COPY ${JAR_FILE} app.jar

# Define the entry point to run the application
ENTRYPOINT ["java","-jar","/app.jar"]
