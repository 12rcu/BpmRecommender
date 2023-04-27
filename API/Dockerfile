FROM openjdk:17 as Builder

# Add backend sources
WORKDIR /src
RUN mkdir api
ADD API /src/api
ADD ../deploy/start.sh /src/api/run/start.sh

# Build jar
WORKDIR /src/api
RUN chmod u+x gradlew
RUN ./gradlew build

# Prepare runtime
FROM openjdk:17-alpine

WORKDIR /run/
COPY --from=Builder /src/api/build/libs/*-all.jar /run/api.jar
COPY --from=Builder /src/api/run/start.sh /run/start.sh

EXPOSE 80

RUN chmod +x /run/start.sh

ENTRYPOINT ["/run/start.sh"]