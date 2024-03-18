FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/Wholesaler-0.0.1-SNAPSHOT.jar /app/Wholesaler-0.0.1-SNAPSHOT.jar
EXPOSE 8080

CMD ["java", "-jar", "Wholesaler-0.0.1-SNAPSHOT.jar"]

