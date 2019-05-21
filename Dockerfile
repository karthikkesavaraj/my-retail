FROM openjdk:8-jre
COPY "build/libs/my-retail-pdp-1.0.0-boot.jar" /
EXPOSE 8080
CMD ["java","-Dspring.profiles.active=docker","-jar","/my-retail-pdp-1.0.0-boot.jar"]
