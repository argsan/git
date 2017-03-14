package com.solace.mqtt.sample;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttCallback;

public class MQTTQueueConsumer {
	public void run(String... args) {
        System.out.println("QoS1Consumer initializing...");

        try {
            // Create an Mqtt client
            MqttAsyncClient mqttClient = new MqttAsyncClient("tcp://192.168.56.101:8001", "HelloWorldQoS1Consumer");
            MqttConnectOptions connOpts = new MqttConnectOptions();
         
            connOpts.setUserName("sample");
            connOpts.setPassword("sample".toCharArray());
            connOpts.setCleanSession(true);
            // Connect the client
            System.out.println("Connecting to Solace broker: tcp://192.168.56.101:8001");
            IMqttToken conToken = mqttClient.connect(connOpts);
            conToken.waitForCompletion(10000);
            if (!conToken.isComplete() || conToken.getException() != null) {
                System.out.println("Error connecting: " + conToken.getException());
                System.exit(-1);
            }
            System.out.println("Connected");

            // Latch used for synchronizing b/w threads
            final CountDownLatch latch = new CountDownLatch(1);

            // Callback - Anonymous inner-class for receiving messages
            mqttClient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Called when a message arrives from the server that
                    // matches any subscription made by the client
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    System.out.println("\nReceived a Message!" +
                            "\n\tTime:    " + time + 
                            "\n\tTopic:   " + topic + 
                            "\n\tMessage: " + new String(message.getPayload()) + 
                            "\n\tQoS:     " + message.getQos() + "\n");
                    latch.countDown(); // unblock main thread
                }

                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to Solace broker lost!" + cause.getMessage());
                    latch.countDown();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });

            // Topic filter the client will subscribe to
            final String subTopic = "Q/tutorial";

            // Subscribe client to the topic filter with QoS level of 1
            System.out.println("Subscribing client to topic: " + subTopic);
            IMqttToken subToken = mqttClient.subscribe(subTopic, 1);
            subToken.waitForCompletion(10000);
            if (!subToken.isComplete() || subToken.getException() != null) {
                System.out.println("Error subscribing: " + subToken.getException());
                System.exit(-1);
            }
            if (subToken.getGrantedQos()[0] != 1) {
                System.out.println("Expected OoS level 1 but got OoS level: " + subToken.getGrantedQos()[0]);
                System.exit(-1);
            }
            System.out.println("Subscribed with OoS level 1 and waiting to receive msgs");

            // Wait for the message to be received
            try {
                latch.await(); // block here until message received, and latch will flip
            } catch (InterruptedException e) {
                System.out.println("I was awoken while waiting");
            }

            // Disconnect the client
//            mqttClient.disconnect();
            System.out.println("Exiting");
            
            MQTTQueueConsumer app = new MQTTQueueConsumer();
            app.run(args);

//            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public static void main(String[] args) {
      
        MQTTQueueConsumer app = new MQTTQueueConsumer();
        app.run(args);
    }
}
