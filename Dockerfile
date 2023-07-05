ARG BASE_IMAGE='java:8'
FROM $BASE_IMAGE
ARG APP_FILE_PATH='*.jar'
ARG RUN_ENV='dev'
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENV profileActive=$RUN_ENV
COPY $APP_FILE_PATH yudao-server.jar
CMD java -Xms512m -Xmx1024m -jar yudao-server.jar --spring.profiles.active=$profileActive
