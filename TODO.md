# Now
- put the generator in docker-compose
  - change it to use an external client config
  - pull the command out of the Dockerfile and provide the java start command in docker-compose.yaml for jet and generator
- set up FFT task in python
- make the audio parameters variable
- send outputs to prometheus so they can be visualized
- output visualization
- add source to the key and introduce multiple sources
- update the data generator to write timestamped audio files
- move generator to rasberry pi (will need to expose ports on laptop)
  could even consider moving the whole thing to a server in my house
- architecture diagram
- make sure telco demo is working

# Later

# Much Later
- fix the the problem with the jet jobs pane of mc
- look for a better implementation of RMS that doesn't involve constructing so many temporary objects
- maven based containers should not be using root user