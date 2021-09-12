nohup java -cp audio-mqtt-prometheus-bridge-1.0-SNAPSHOT.jar com.sorintlab.jet.data.acquisition.audio.MQTTPrometheusBridge \
	--aws-mqtt-endpoint a17sav9lrv8l6k-ats.iot.us-east-2.amazonaws.com \
	--aws-mqtt-client-id prometheus \
	--aws-mqtt-topic audio_summaries \
	--aws-client-cert-file certificate.pem.crt \
        --aws-client-private-key-file private.pem.key	< /dev/null  \
				> /tmp/mqtt-promethus.log 2>&1 &
