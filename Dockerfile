FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/*.jar to-do-app.jar
ENTRYPOINT ["java","-jar","to-do-app.jar"]
EXPOSE 8080