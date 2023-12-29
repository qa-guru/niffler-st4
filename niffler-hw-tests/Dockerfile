ARG DOCKER
FROM $DOCKER
ENV JAVA_OPTS=""

WORKDIR /niffler
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY ./niffler-hw-tests ./niffler-hw-tests
COPY ./niffler-grpc-common ./niffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

CMD chmod +x ./niffler-hw-tests/send_results.sh; ./gradlew test -Dtest.env=docker -Drepository=jpa; bash ./niffler-hw-tests/send_results.sh
