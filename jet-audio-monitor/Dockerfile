FROM maven:3-openjdk-11
# COPY --from=hazelcast/hazelcast-jet /opt/hazelcast-jet /opt/hazelcast-jet
# RUN  mv /opt/hazelcast-jet/opt/hazelcast-jet-python-4.3.jar /opt/hazelcast-jet/lib
WORKDIR /jet-audio-monitor
RUN apt-get update && apt-get install -y python3 python3-pip
COPY src ./src/
COPY pom.xml ./
RUN mvn package
COPY python ./python/
COPY hazelcast.yaml ./

