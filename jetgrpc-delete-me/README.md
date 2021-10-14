# Greengrass Deployment Instructions

A recipe is included in this directory.  Hazelcast Jet and the jet configuration 
file in this project: `config/hazelcast.yaml` must be uploaded to the s3 location 
mentioned in the recipe.

To build the Jet upload, start with the Hazelcast Jet Slim distribution , download 
the gRPC jar separately and place it in the lib folder of the Hazelcst distro.  
Then zip everything up and uplpad to s3.