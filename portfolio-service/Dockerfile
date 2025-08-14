
FROM postgres:latest

FROM maven:3.9.9 AS build
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package -DskipTests

FROM openjdk:17 AS prod
COPY --from=build target/portfolio-service-0.0.1-SNAPSHOT.jar portfolio-service.jar
EXPOSE 9000

ENTRYPOINT ["java", "-jar", "portfolio-service.jar"]