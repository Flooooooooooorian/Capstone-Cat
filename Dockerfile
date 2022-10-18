FROM openjdk:17

ENV ENVIRONMENT=prod

MAINTAINER Florian Weber <florian.weber@neuefische.de>

ADD backend/target/app.jar app.jar

CMD [ "sh", "-c", "java -Dspring.data.mongodb.uri=$MONGO_DB_URI -jar /app.jar" ]
