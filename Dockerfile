# Use the official OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Copy the packaged jar file into the container
COPY target/banking-finance-0.0.1-SNAPSHOT.jar banking-finance.jar

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/banking-finance.jar"]
