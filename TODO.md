# Now
- change the generator to support multiple streams
- change the sample rate to 200 KHz
- change the job to support multiple machines (key by machine id)

# Later
- send outputs to prometheus so they can be visualized
- output visualization
- architecture diagram
- add some alerts
- separate out the jet job from the jet servers
- make it a multi-node jet cluster
- lock down the versions of images
- add source to the key and introduce multiple sources
- move generator to rasberry pi (will need to expose ports on laptop)
  could even consider moving the whole thing to a server in my house

# Much Later
- fix the the problem with the jet jobs pane of mc
- look for a better implementation of RMS that doesn't involve constructing so many temporary objects
- maven based containers should not be using root user

# Notes
- integrate the python pipeline
  - get frigging python working!
    - does it have to be absolute path ? NO
    - do I need guava ?  NO
    - does it need to be on the cp ? NO
    - is it required to set the main class ? NO
    - does it have to be 4.3 ? NO
