FROM openjdk:17-jdk-alpine
RUN mkdir /static_files
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]