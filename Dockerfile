FROM openjdk:11-jdk-stretch
VOLUME /var/lib/mindmath-cloud/config-repo
COPY build/libs/MindMath-0.0.2-SNAPSHOT.war /opt/mindmath-cloud/lib/
ENTRYPOINT ["java","-jar","/opt/mindmath-cloud/lib/MindMath-0.0.2-SNAPSHOT.war"]