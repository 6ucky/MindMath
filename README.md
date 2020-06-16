# MindMathServer

## Run server with Gradle
```
./gradlew bootRun
```

## Package jar file with Gradle
```
./gradlew bootJar
```

## Package war file with Gradle
```
./gradlew bootWar
```
## Build Docker Image
```
sudo docker build --build-arg JAR_FILE=build/libs/MindMath-0.0.1-SNAPSHOT.war -t springio/gs-spring-boot-docker .
sudo ./gradlew jibDockerBuild --image=springio/gs-spring-boot-docker
```
## Run Docker Image
```
sudo docker run -p 8080:8080 -t springio/gs-spring-boot-docker
```
## Run Docker Compose
```
sudo docker-compose up --build
```
## Stop Docker Compose
```
sudo docker-compose down
```
## POST request
```
POST http://localhost:8080/task
Content-Type: application/json
Version-LIP6: 1.0
Authorization: mocah
```
## Swagger Documentation
```
http://localhost:8080/swagger-ui.html
```
## Infos

- Class diagram build with [ObjectAid UML Explorer](https://www.objectaid.com/home)
