FROM openjdk:11-jdk-stretch
ARG JAR_FILE=target/MindMath-0.0.1-SNAPSHOT.war
COPY ${JAR_FILE} MindMath-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java","-jar","/MindMath-0.0.1-SNAPSHOT.war"]