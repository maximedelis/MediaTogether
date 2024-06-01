# Build environment
FROM gradle:8.4.0-jdk17-alpine AS builder

# Copy build.gradle, settings.gradle and gradlew
COPY build.gradle settings.gradle gradlew ./

# Copy src directory
COPY src ./src

# Copy gradle directory
COPY gradle ./gradle

# Create JAR
RUN ./gradlew clean bootJar

# Minimal RE
FROM openjdk:17-alpine

# Copy jar to the RE
COPY --from=builder /home/gradle/build/libs/mediatogether-0.0.1-SNAPSHOT.jar mediatogether.jar

EXPOSE 8080

CMD ["java", "-jar", "mediatogether.jar"]
