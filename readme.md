# Overview

tbc

# Getting the sample running

1. Install MySQL 5.7 database in your system and Java 8 and eclipse (Version: Neon.1a Release (4.6.1)) , Maven and its eclipse plugin & Tomcat 9 / or any other web server[I have used these for my local setup]
2. Please set the root MySQL user password as admin
3. Import these two project in your eclipse workspace (Solace-Demo is the web paho project & MQTT-Client is the data provider)
4. Logon to MySQL command line using root and run the IRCTC.sql file
5. Compile all java component
6. Once all setup is done please Run the com.solace.irctc.messageq.IRCTCMessageManager (Please make sure your system is connected to the internet)
7. Add the web project into tomcat / or any other web server and start the server
8. Hit the url http://<YOUR HOST>:<YOUR PORT>/Solace-Demo/index.html
