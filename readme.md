 Here is how to set-up the project in a local system.
•	Install MySQL 5.7 database in your system                                                                               
•	Java 8 and eclipse (Version: Neon.1a Release (4.6.1))                                                                                   
•	Maven and its eclipse plugin                                                                                                       
•	Tomcat 9 / or any other web server[I have used these for my local setup]                                                       
•	Please set the root MySQL user password as admin                                                                                  
•	Import these two project in your eclipse workspace (Solace-Demo is the web paho project & MQTT-Client is the data provider)             
•	Logon to MySQL command line using root and run the IRCTC.sql file,this will create the metadata tables                                   
•	Compile all java component                                                                                                              
•	Once all setup is done please Run the com.solace.irctc.messageq.DataFechingFactory (Please make sure your system is connected to the internet)                                                                                                                                
•	Add the web project into tomcat / or any other web server and start the server                                                          
•	In code please change the router IP and port accordingly                                                                                
•	For router configuration Please run the cli script located under Solace-Demo file name Solace-Demo/Sample-Message-VPM.cli.   

Code Changes
Class - com.solace.demo.messageq.DataFechingFactory                                                                                       
Method - consumeMessage()                                                                                                                 
properties.setProperty(JCSMPProperties.HOST, ""); // msg-backbone ip                                                                       
properties.setProperty(JCSMPProperties.VPN_NAME, ""); // message-vpn                                                                       
properties.setProperty(JCSMPProperties.USERNAME, ""); // client-username                                                                   
properties.setProperty(JCSMPProperties.PASSWORD, ""); // password  
//Put the queue Name
final String queueName = "";
Method - publishMessage(String dataString)
MqttClient mqttClient = new MqttClient("tcp://<IP>:<port>", "<Client Id>");
MqttConnectOptions connOpts = new MqttConnectOptions();
connOpts.setUserName(""); // client-username   
connOpts.setPassword("".toCharArray()); // client-password  

JavaScript File  - Controller.js                                                                                                         
Function - myOtherModule.controller  and myOtherModule.controller                                                                       
var ip = ""; //Host IP                                                                                                                   
var port = ""; //MQTT port                                                                                                               
var id = ""; //MQTT Client ID                                                                                                           




How to run the application                                                                                                                
•	Make the necessary changes to the router ip and port                                                                                   
•	Run the file DataFechingFactory. Or buid a executable jar and then run the same                                                         
•	Start your webserver where you have deployed Solace-Demo application                                                                    
•	Hit the url http://host:port/Solace-Demo/index.html                                                                                      
