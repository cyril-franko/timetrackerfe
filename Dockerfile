FROM openjdk:8-jdk-alpine
MAINTAINER Cyril Franko <cyril.franko@gmail.com>
VOLUME /tmp

ADD ./target/timetracker-web-app-0.1.0.RELEASE.jar timetracker.jar

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /timetracker.jar" ]

EXPOSE 8081
