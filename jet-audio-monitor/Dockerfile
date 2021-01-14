FROM maven:3-openjdk-11
WORKDIR /jet-audio-monitor
COPY src ./src/
COPY pom.xml ./
RUN mvn package
COPY hazelcast.yaml hazelcast-client.yaml ./
