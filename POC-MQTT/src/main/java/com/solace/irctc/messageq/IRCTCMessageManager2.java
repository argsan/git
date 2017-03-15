package com.solace.irctc.messageq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.codehaus.jackson.map.ObjectMapper;

import com.solace.dto.Days;
import com.solace.dto.Record;
import com.solace.dto.TrainDetailsDTO;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageListener;
import com.solacesystems.jcsmp.XMLMessageProducer;

public class IRCTCMessageManager2 {

    public static void main(String... args) throws JCSMPException, InterruptedException {
    	consumeMessage();
    }
    
    public static void consumeMessage() throws JCSMPException, InterruptedException
    {
        final List <String> outputStr= new ArrayList<String>();
    	      
        System.out.println("QueueConsumer initializing...");
        // Create a JCSMP Session
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, "192.168.56.101"); // msg-backbone ip:port
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
                System.out.printf("Message Dump:%n%s%n", msg.dump());
                
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
        publishMessage(outputStr.toString());
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
	            System.out.println(jsonStr.substring(0, 0));
	            return jsonStr;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  
		return null;
    	
    }
    
    public static void publishMessage(String message) throws JCSMPException, InterruptedException {
        // Check command line arguments
        System.out.println("QueueProducer initializing...");
        // Create a JCSMP Session
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, "192.168.56.101"); // msg-backbone ip:port
        properties.setProperty(JCSMPProperties.VPN_NAME, "Sample-Message-VPM"); // message-vpn
        properties.setProperty(JCSMPProperties.USERNAME, "sample"); // client-username
        properties.setProperty(JCSMPProperties.PASSWORD, "sample"); // 
        final JCSMPSession session = JCSMPFactory.onlyInstance().createSession(properties);
        session.connect();

        final Topic topic = JCSMPFactory.onlyInstance().createTopic("topic/respTopic");
        /** Anonymous inner-class for handling publishing events */
        XMLMessageProducer prod = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",
                        messageID,timestamp,e);
            }
        });
        // Publish-only session is now hooked up and running!

        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        final String text = loadData();
        msg.setText(text);
        System.out.printf("Connected. About to send message '%s' to topic '%s'...%n",text,topic.getName());
      
        	 prod.send(msg,topic);
        	 Thread.sleep(250);

       
        System.out.println("Message sent. Exiting.");
        consumeMessage();
    }
}