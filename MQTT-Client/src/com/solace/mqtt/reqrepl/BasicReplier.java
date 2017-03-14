package com.solace.mqtt.reqrepl;

import java.util.concurrent.CountDownLatch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 * A Mqtt basic replier
 *
 */
public class BasicReplier {
    private JSONParser parser = new JSONParser();
    
    public void run(String... args) {
        System.out.println("BasicReplier initializing...");

        try {
            // Create an Mqtt client
            final MqttClient mqttClient = new MqttClient("tcp://192.168.56.101:8001", "HelloWorldBasicReplier");
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("sample");
            connOpts.setPassword("sample".toCharArray());
            connOpts.setCleanSession(true);
            
            // Connect the client
            System.out.println("Connecting to Solace broker: tcp://192.168.56.101:8001");
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // Latch used for synchronizing b/w threads
            final CountDownLatch latch = new CountDownLatch(1);
            
            // Topic filter the client will subscribe to receive requests
            final String requestTopic = "T/GettingStarted/request";
            
            // Callback - Anonymous inner-class for receiving request messages
            mqttClient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    try {
                        // Parse the received request message and convert payload to a JSONObject
                        Object payloadObj = parser.parse(new String(message.getPayload()));
                        JSONObject jsonPayload = (JSONObject) payloadObj;
                        
                        // Get the correlationId and replyTo fields from the payload
                        String correlationId = (String) jsonPayload.get("correlationId");
                        String replyTo = (String) jsonPayload.get("replyTo");
                        String messageContent = (String) jsonPayload.get("message");
                        
                        System.out.println("\nReceived a request message!" +
                            "\n\tCorrel. Id: " + correlationId + 
                            "\n\tReply To:   " + replyTo + 
                            "\n\tMessage:    " + messageContent + "\n");
                    
                        // Create the response payload in JSON format and set correlationId
                        // to the id received in the request message above. Requestor will
                        // use this to correlate the response with its request message.
                        JSONObject obj = new JSONObject();
                        obj.put("correlationId", correlationId);
                        obj.put("message", "Sample Response");
                        String respPayload = obj.toJSONString();
                        
                        // Create a response message and set the response payload
                        MqttMessage respMessage = new MqttMessage(respPayload.getBytes());
                        respMessage.setQos(0);
                
                        System.out.println("Sending response to: " + replyTo);
                
                        // Publish the response message to the replyTo topic retrieved 
                        // from the request message above
                        MqttTopic mqttTopic = mqttClient.getTopic(replyTo);
                        mqttTopic.publish(respMessage);
                        
                        latch.countDown(); // unblock main thread
                    } catch (ParseException ex) {
                        System.out.println("Exception parsing request message!");
                        ex.printStackTrace();
                    }
                }

                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to Solace broker lost!" + cause.getMessage());
                    latch.countDown();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });
            
            // Subscribe client to the topic filter with a QoS level of 0
            System.out.println("Subscribing client to request topic: " + requestTopic);
            mqttClient.subscribe(requestTopic, 0);

            System.out.println("Waiting for request message...");
            // Wait for till we have received a request and sent a response
            try {
                latch.await(); // block here until message received, and latch will flip
            } catch (InterruptedException e) {
                System.out.println("I was awoken while waiting");
            }
            
            // Disconnect the client
            mqttClient.disconnect();
            System.out.println("Exiting");

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
     
        
        BasicReplier app = new BasicReplier();
		app.run(args);
    }
}