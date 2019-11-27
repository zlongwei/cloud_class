FROM openjdk:8-jdk-alpine
MAINTAINER study.163.com <422539792@qq.com>
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY wait-for-it.sh /wait-for-it.sh
# RUN chmod -R 777 /app.jar
EXPOSE 8765
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
