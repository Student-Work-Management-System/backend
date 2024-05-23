FROM openjdk:21

WORKDIR /app

COPY StudentWorkManagementSystem-0.0.1-SNAPSHOT.jar .

RUN cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime

CMD ["java", "-jar", "StudentWorkManagementSystem-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
 
EXPOSE 8080
