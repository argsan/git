package com.solace.irctc.messageq;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.solace.mqtt.domain.GenericDataManager;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;

public class IRCTCMessageManager {

    public static void main(String... args) throws JCSMPException, InterruptedException {
    	consumeMessage();
    }
    
    public static void consumeMQTTMessage() throws JCSMPException, InterruptedException
    {
    	 final List <String> outputStr= new ArrayList<String>();
    	try {
            // Create an Mqtt client

            MqttAsyncClient mqttClient = new MqttAsyncClient("", "HelloWorldQoS1Consumer");
            MqttConnectOptions connOpts = new MqttConnectOptions();
         
            connOpts.setUserName("sample");
            connOpts.setPassword("sample".toCharArray());
            connOpts.setCleanSession(true);
            // Connect the client
            System.out.println("Connecting to Solace broker: tcp://");
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
                    String outputchr = new String(message.getPayload()) ;
                    outputStr.add(0, outputchr);
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
            if(outputStr !=null && outputStr.size()>0)
                   publishMessage(outputStr.get(0));
 

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
    public static void consumeMessage() throws JCSMPException, InterruptedException
    {
        final List <String> outputStr= new ArrayList<String>();
    	      
        System.out.println("QueueConsumer initializing...");
        // Create a JCSMP Session
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, ""); // msg-backbone ip:port
        properties.setProperty(JCSMPProperties.VPN_NAME, "Sample-Message-VPM"); // message-vpn
        properties.setProperty(JCSMPProperties.USERNAME, "sample"); // client-username
        properties.setProperty(JCSMPProperties.PASSWORD, "sample"); // 
        final JCSMPSession session = JCSMPFactory.onlyInstance().createSession(properties);
        session.connect();

        final String queueName = "Q/IRCTC-Req-Q";
        System.out.printf("Attempting to provision the queue '%s' on the appliance.%n", queueName);
        final EndpointProperties endpointProps = new EndpointProperties();
        // set queue permissions to "consume" and access-type to "exclusive"
        endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
        endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);
        // create the queue object locally
        final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);
        // Actually provision it, and do not fail if it already exists
        session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

        final CountDownLatch latch = new CountDownLatch(1); // used for synchronizing b/w threads

        System.out.printf("Attempting to bind to the queue '%s' on the appliance.%n", queueName);

        // Create a Flow be able to bind to and consume messages from the Queue.
        final ConsumerFlowProperties flow_prop = new ConsumerFlowProperties();
        flow_prop.setEndpoint(queue);
        flow_prop.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

        EndpointProperties endpoint_props = new EndpointProperties();
        endpoint_props.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);

        final FlowReceiver cons = session.createFlow(new XMLMessageListener() {
            public void onReceive(BytesXMLMessage msg) {
                if (msg instanceof TextMessage) {
                    System.out.printf("TextMessage received: '%s'%n", ((TextMessage) msg).getText());
                } else {
                	
                    System.out.println("Message received.");
                }
             
                String outputchr = new String(msg.dump());
                outputStr.add(0, outputchr);

                // When the ack mode is set to SUPPORTED_MESSAGE_ACK_CLIENT,
                // guaranteed delivery messages are acknowledged after
                // processing
                msg.ackMessage();
                latch.countDown(); // unblock main thread
            }

            public void onException(JCSMPException e) {
                System.out.printf("Consumer received exception: %s%n", e);
                latch.countDown(); // unblock main thread
            }
        }, flow_prop, endpoint_props);

        // Start the consumer
        System.out.println("Connected. Awaiting message ...");
        cons.start();

        try {
            latch.await(); // block here until message received, and latch will flip
        } catch (InterruptedException e) {
            System.out.println("I was awoken while waiting");
        }
        // Close consumer
        cons.close();
        System.out.println("Exiting.");
        if(outputStr !=null && outputStr.size()>0)
        {
        	System.out.println(outputStr.get(0));
        	String ss1 = outputStr.get(0);
        	String ss2 =  outputStr.get(0).substring(ss1.indexOf("table"), ss1.length());
        	System.out.println(ss2);
        	 publishMessage(ss2);
        }
           
    }
    
    
    
    	
    
    public static void publishMessage(String dataString) throws JCSMPException, InterruptedException {
        // Check command line arguments
        System.out.println("QueueProducer initializing...");
        
        try {
            // Create an Mqtt client
            MqttClient mqttClient = new MqttClient("tcp://", "HelloWorldQoS1Producer");
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("sample");
            connOpts.setPassword("sample".toCharArray());
            connOpts.setCleanSession(true);
            // Connect the client
            System.out.println("Connecting to Solace broker: tcp://" + "tcp://");
            mqttClient.connect(connOpts);
            System.out.println("Connected");
            // Create a Mqtt message
            
            
            String table="";
            String whereCl="";
            if(dataString.indexOf("|") >=0)
            {
            	   StringTokenizer st = new StringTokenizer(dataString, "|"); 
                   while(st.hasMoreTokens()) { 
                  	table = st.nextToken(); 
                  	whereCl = st.nextToken(); 
                  }
            }
            else
            {
            	table = dataString;
            }
         

            MqttMessage message = new MqttMessage(GenericDataManager.loadData(table,whereCl).getBytes());
            
            // Set the QoS on the Messages - 
            // Here we are using QoS of 1 (equivalent to Persistent Messages in Solace)
            message.setQos(1);

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
}