FROM openjdk:17-jdk-slim

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/notification-service-0.0.1-SNAPSHOT.jar"]