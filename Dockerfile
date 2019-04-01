FROM openjdk:11.0.2-jdk
MAINTAINER Jie
LABEL app="demo" version="0.0.1" by="Jie"
COPY target/demoforcicd-1.0-SNAPSHOT.jar demoforcicd-1.0-SNAPSHOT.jar
CMD java -jar demoforcicd-1.0-SNAPSHOT.jar
