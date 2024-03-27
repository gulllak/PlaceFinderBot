FROM amazoncorretto:21-alpine-jdk
COPY target/*.jar PlaceFinder.jar
ENTRYPOINT ["java","-jar","PlaceFinder.jar"]