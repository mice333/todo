FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/todo.jar todo.jar

EXPOSE 8080

CMD ["java", "-jar", "todo.jar"]