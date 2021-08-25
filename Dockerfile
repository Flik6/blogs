FROM openjdk:8-jdk-alpine
ADD /target/*.jar app.jar
CMD 'touch /app.jar'
VOLUME /tmp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8081