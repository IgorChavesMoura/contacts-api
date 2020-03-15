FROM openjdk:8

COPY target/contacts-api.jar /app/contacts-api.jar

WORKDIR /app

CMD ["java", "-jar", "contacts-api.jar", "--spring.profiles.active=prod"]