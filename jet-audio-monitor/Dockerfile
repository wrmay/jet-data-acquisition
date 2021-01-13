FROM maven:3-openjdk-11
WORKDIR /jet-audio-monitor
COPY pom.xml hazelcast.yaml ./
COPY src ./src/
RUN mvn package
CMD ["java", "-cp", "target/jet-audio-monitor-1.0-SNAPSHOT.jar:target/dependency/*", "-Xmx2500m", "-Xms2500m", "-Dhazelcast.config=./hazelcast.yaml", "com.sorintlab.jet.data.acquisition.audio.MonitoringJob"]
