# Build environment
FROM amazoncorretto:21-alpine-jdk AS builder

RUN cd /home

# Copy build.gradle, settings.gradle and gradlew
COPY build.gradle settings.gradle gradlew ./

# Copy src directory
COPY src ./src

# Copy gradle directory
COPY gradle ./gradle

# Create JAR
RUN ./gradlew clean bootJar

# Minimal RE
FROM amazoncorretto:21-alpine-jdk

RUN cd /home

# Copy jar to the RE
COPY --from=builder ./build/libs/MediaTogether-0.0.1-SNAPSHOT.jar mediatogether.jar

EXPOSE 8080

CMD ["java", "-jar", "mediatogether.jar"]
