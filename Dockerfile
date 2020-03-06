FROM openjdk:11-jdk-stretch
VOLUME /var/lib/spring-cloud/config-repo
COPY build/libs/MindMath-0.0.1-SNAPSHOT.war /opt/spring-cloud/lib/
ENTRYPOINT ["java","-jar","/opt/spring-cloud/lib/MindMath-0.0.1-SNAPSHOT.war"]