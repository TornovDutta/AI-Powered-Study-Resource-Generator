FROM eclipse-temurin:17-jdk
COPY target/StudyResourceGenerator.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
