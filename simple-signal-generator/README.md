Use ` aws iot describe-endpoint --endpoint-type iot:Data-ATS` to get the data endpoint for your account.
ATS refers to the certificate installed on the server.

Steps
1. Make a certificate in the "Security/Certificates section of the IoT console"
2. Download the cert and the public key and the private key , activate the certificate.
3. Attach a policy to the cert you just created: GreengrassV2IoTThingPolicy
4. Retrieve the endpoint to use with the aws SDK command: 

```
(venv) MacBook-Pro:BasicPubSub rmay_sorint$ aws iot describe-endpoint --endpoint-type iot:Data-ATS
{
"endpointAddress": "a17sav9lrv8l6k-ats.iot.us-east-2.amazonaws.com"
}
```

5. You can now use the BasicPubSub client, provided here: https://github.com/aws/aws-iot-device-sdk-java-v2 
   to send messages.  Note the "ats" endpoint is  specified.  See the example below:
   
```
java  -jar target/BasicPubSub-1.0-SNAPSHOT.jar --endpoint a17sav9lrv8l6k-ats.iot.us-east-2.amazonaws.com --port 443   --cert auth/2ffa1c1a83-certificate.pem.crt  --key auth/2ffa1c1a83-private.pem.key   --topic TEST --message "Hello MQTT" 
```

# Painful Lesson Learned

The exception with message "Old requests from the previous session are cancelled, and offline request will not be accept." 
happened often. Reducing the message size  from ~400k to ~90k appears to have resolved the problem.  

Documentation is not great. Specifically, I can't tell whether MQTT client connections can be shared. In the end,
I dodged the question by creating only one instance of the MqttClient and MqttClientConnection and accessed them 
only from a single thread (hence the SingleThreadScheduledExecutor).

# References
- on endpoints: https://docs.aws.amazon.com/iot/latest/developerguide/server-authentication.html?icmpid=docs_iot_console#server-authentication-certs 
- on ports: https://docs.aws.amazon.com/iot/latest/developerguide/protocols.html

