# Features
- expose Jet metrics via Prometheus
- send outputs to Prometheus so they can be visualized
- output visualization
- architecture diagram
- add some alerts
- MQTT source

# Possible optimizations
- look for a better implementation of RMS that doesn't involve constructing so many temporary objects
- Gson vs. Jackson
- Consider a less expensive format to move between Java and Python like protobuf
- Implement a Stream Serializer

