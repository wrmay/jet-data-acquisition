FROM hazelcast/hazelcast-jet
WORKDIR /opt/hazelcast-jet
RUN mv opt/hazelcast-jet-python-4.3.jar lib
RUN apt-get update && apt-get install -y python3 python3-pip
COPY python python/
COPY hazelcast.yaml ./
ENV JAVA_OPTS="-Xmx2g -Xms2g -Dhazelcast.config=/opt/hazelcast-jet/hazelcast.yaml"