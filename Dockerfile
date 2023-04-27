FROM gradle:7.6.1-jdk17 as Builder

# Add backend sources
WORKDIR /src
RUN mkdir api
ADD . /src/api
ADD start.sh /src/api/run/start.sh

# Build jar
WORKDIR /src/api
RUN chmod u+x gradlew
RUN ./gradlew build --exclude-task test

# Prepare runtime
FROM openjdk:17-alpine

WORKDIR /run/
COPY --from=Builder /src/api/API/build/libs/*-all.jar /run/api.jar
COPY --from=Builder /src/api/API/run/start.sh /run/start.sh

EXPOSE 80

RUN chmod +x /run/start.sh

ENTRYPOINT ["/run/start.sh"]