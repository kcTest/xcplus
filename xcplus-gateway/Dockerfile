FROM openjdk:17.0.2-jdk-slim
ARG JAR_FILE
ENV JAVA_OPTS=""
ADD target/${JAR_FILE} app.jar
RUN echo "Asia/Shanghai" > /etc/timezone
EXPOSE 1006
ENTRYPOINT java ${JAVA_OPTS}  -Dspring.profiles.active=dev -jar app.jar