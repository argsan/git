package com.solace.mqtt;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.solace.dto.Days;
import com.solace.dto.Record;
import com.solace.dto.TrainDetailsDTO;

/**
 * A Mqtt QoS1 message producer 
 *
 */
public class MQTTQueueServerManager {

    public static void publishMessage(String inputMessage) {
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
            String content = loadData();
            MqttMessage message = new MqttMessage(content.getBytes());
            // Set the QoS on the Messages - 
            // Here we are using QoS of 1 (equivalent to Persistent Messages in Solace)
            message.setQos(1);
            System.out.println("Publishing message: " + content);
            // Publish the message
            mqttClient.publish("T/Booking/resp", message);
            // Disconnect the client
            mqttClient.disconnect();
            System.out.println("Message published. Exiting");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
        consumeMessage();
    }
    
    @SuppressWarnings("unchecked")
	public static String loadData()
    {
    	ArrayList <TrainDetailsDTO> trainLst = new ArrayList<TrainDetailsDTO>();
    	Record rec = new Record();
    	TrainDetailsDTO trnDto = new TrainDetailsDTO("25657", "KANCHANJANGA EX", "SEALDAH", "NEW JALPAIGURI", "06:35", "18:15", "11:40", "6", null);
    	Days day = new Days("Y","Y","Y","Y","Y","N","Y");
       	trnDto.setDays(day);
    	trainLst.add(trnDto);
    	trnDto = new TrainDetailsDTO("13141", "TESTA TORSA EXP", "SEALDAH", "NEW JALPAIGURI", "13:40", "02:40", "13:00", "6", null);
    	day = new Days("Y","Y","Y","Y","Y","N","Y");
    	trnDto.setDays(day);
    	trainLst.add(trnDto);
    	rec.setRecords(trainLst);
    	   ObjectMapper mapperObj = new ObjectMapper();
	        try {
	            String jsonStr = mapperObj.writeValueAsString(rec);
//	            String jsonStr1 = jsonStr.substring(0, 0);
	           
	            System.out.println(jsonStr.substring(0, 0));
	            return jsonStr;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  
		return null;
    	
    }
    public static void consumeMessage() {
        System.out.println("QoS1Consumer initializing...");
        final List <String> outputStr= new ArrayList<String>();
		
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
                    String outputchr = new String(message.getPayload());
                    outputStr.add(0, outputchr);
                    
                }

                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to Solace broker lost!" + cause.getMessage());
                    latch.countDown();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });

            // Topic filter the client will subscribe to
            final String subTopic = "T/Booking/request";

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

//            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
        publishMessage(outputStr.toString());
    }

    public static void main(String[] args) {
    	consumeMessage();
    }
}