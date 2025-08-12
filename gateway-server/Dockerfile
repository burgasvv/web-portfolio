
FROM maven:4.0.0 AS build
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package -DskipTests

FROM openjdk:17 AS prod
COPY --from=build target/gateway-server-0.0.1-SNAPSHOT.jar gateway-server.jar
EXPOSE 8765

ENTRYPOINT ["java", "-jar", "gateway-server.jar"]