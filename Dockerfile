FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/moneyTransferTask*.jar money-transfer-service.jar
EXPOSE 7000
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar money-transfer-service.jar