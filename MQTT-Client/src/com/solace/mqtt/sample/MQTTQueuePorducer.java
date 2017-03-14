package com.solace.mqtt.sample;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * A Mqtt QoS1 message producer 
 *
 */
public class MQTTQueuePorducer {

    public void run(String... args) {
        System.out.println("QoS1Producer initializing...");

        try {
            // Create an Mqtt client
            MqttClient mqttClient = new MqttClient("tcp://192.168.56.101:8001", "HelloWorldQoS1Producer");
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("sample");
            connOpts.setPassword("sample".toCharArray());
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println("Connecting to Solace broker: tcp://" + "tcp://192.168.56.101:8001");
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // Create a Mqtt message
            String content = "{\"fromStn\":null,\"toStn\":null,\"jClass\":null,\"jDate\":null}";
            MqttMessage message = new MqttMessage(content.getBytes());
            // Set the QoS on the Messages - 
            // Here we are using QoS of 1 (equivalent to Persistent Messages in Solace)
            message.setQos(1);

            System.out.println("Publishing message: " + content);

            // Publish the message
            mqttClient.publish("T/GettingStarted/request", message);

            // Disconnect the client
            mqttClient.disconnect();

            System.out.println("Message published. Exiting");

            System.exit(0);
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
      

        MQTTQueuePorducer app = new MQTTQueuePorducer();
		app.run(args);
    }
}