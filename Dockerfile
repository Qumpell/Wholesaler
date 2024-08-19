FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /build/target/Wholesaler-0.0.1-SNAPSHOT.jar /app/Wholesaler-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "Wholesaler-0.0.1-SNAPSHOT.jar"]
