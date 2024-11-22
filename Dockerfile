FROM openjdk:17-jdk-slim

COPY build/libs/hamburgersee-0.0.1-SNAPSHOT.jar /usr/app/hamburgerbwa-0.0.1.jar

WORKDIR /usr/app

CMD ["java", "-jar", "hamburgerbwa-0.0.1.jar"]
