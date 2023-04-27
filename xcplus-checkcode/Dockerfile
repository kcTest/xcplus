#openjdk:17.0.2-jdk-slim/openjdk:17.0.2-jdk-buster(包含checkcode字体相关依赖)
FROM openjdk:17.0.2-jdk-buster
#如果指定 ARG  构建镜像时还需要指定参数值:docker build --build-arg JAR_FILE=target/*.jar -t checkcode:1.0 . 
ARG JAR_FILE
#接收变量
ENV JAVA_OPTS=""
ADD target/${JAR_FILE} app.jar
RUN echo "Asia/Shanghai" > /etc/timezone
EXPOSE 1020
# -Dfile.encoding=utf-8 指定文件识别格式 
# docker run -p 8080:8080 --env JAVA_OPTS=-Dlogging.level.org.springframework=DEBUG <image_tag> 
ENTRYPOINT java ${JAVA_OPTS}  -Dspring.profiles.active=dev -jar app.jar