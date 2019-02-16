package com.listener.application;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.io.IOException;

@Slf4j
class MqttController implements MqttCallback {



    private final String brokerUrl;
    private final boolean clean;
    private final String userName;
    private final String password;
    private MqttConnectOptions conOpt;
    private MqttAsyncClient client;

    public void messageArrived(String topic, MqttMessage message) {

        log.info("Otrzymano wiadomosc z tematu {} o tresci : {}", topic, message.toString());

        String frame = new String(message.getPayload());

        HeaderParser headerParser = new HeaderParser(frame);

        log.info("Widomosc od modulu {} dla modulu {}, typ wiadomosci {} ", headerParser.getTransmitter(), headerParser.getReceiver(), headerParser.getMessageType());

        int messageType = headerParser.getMessageType();
        boolean processFlag = headerParser.toProcess();

        if (processFlag) {

            switch (messageType) {

                case 0:
                    Installation installation = new Installation();
                    installation.parse(frame, headerParser.getTransmitter());
                    installation.persist();

                    break;

                case 1:
                    break;


            }


        } else {
            log.info("Message from Unit {} isn't for server", headerParser.getTransmitter());
        }


    }

    public void deliveryComplete(IMqttDeliveryToken token) {

        try {
            log.info("Delivery complete callback: Publish Completed");
        } catch (Exception ex) {
            log.info("Exception in delivery complete callback" + ex);
        }
    }

    public void connectionLost(Throwable cause) {
        // Called when the connection to the server has been lost.
        // An application may choose to implement reconnection
        // logic at this point. This sample simply exits.
        log.info("Connection to " + brokerUrl + " lost!" + cause);
        System.exit(1);
    }

     MqttController(String brokerUrl, String clientId, boolean cleanSession, String userName, String password) throws MqttException {
        this.brokerUrl = brokerUrl;
        this.clean = cleanSession;
        this.userName = userName;
        this.password = password;
        //This sample stores in a temporary directory... where messages temporarily
        // stored until the message has been delivered to the server.
        //..a real application ought to store them somewhere
        // where they are not likely to get deleted or tampered with
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

        try {
            // Construct the connection options object that contains connection parameters
            // such as cleanSession and LWT
            conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(clean);
            if (password != null) {
                conOpt.setPassword(this.password.toCharArray());
            }
            if (userName != null) {
                conOpt.setUserName(this.userName);
            }

            // Construct a non-blocking MQTT client instance
            client = new MqttAsyncClient(this.brokerUrl, clientId, dataStore);

            // Set this wrapper as the callback handler
            client.setCallback(this);

        } catch (MqttException e) {
            log.error(e.toString());
            log.info("Unable to set up client: " + e.toString());
            System.exit(1);
        }
    }

     boolean publish(String topicName, int qos, byte[] payload) throws MqttException {

        // Connect to the MQTT server
        // issue a non-blocking connect and then use the token to wait until the
        // connect completes. An exception is thrown if connect fails.
        String s = new String(payload);
        log.info("Connecting to " + brokerUrl + " with client ID " + client.getClientId());
        IMqttToken conToken = client.connect(conOpt, null, null);
        conToken.waitForCompletion();

        log.info("Connected");


        log.info("Publishing  to topic {} fallowing message : {}", topicName, s);

        // Construct the message to send
        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);

        // Send the message to the server, control is returned as soon
        // as the MQTT client has accepted to deliver the message.
        // Use the delivery token to wait until the message has been
        // delivered
        IMqttDeliveryToken pubToken = client.publish(topicName, message, null, null);
        pubToken.waitForCompletion();
        log.info("Published");

        // Disconnect the client
        // Issue the disconnect and then use a token to wait until
        // the disconnect completes.
        IMqttToken discToken = client.disconnect(null, null);
        discToken.waitForCompletion();
        log.info("Publish client disconnected");
        return true;
    }

     void subscribe(String topicName, int qos) throws MqttException {

        // Connect to the MQTT server
        // issue a non-blocking connect and then use the token to wait until the
        // connect completes. An exception is thrown if connect fails.
        log.info("Connecting to " + brokerUrl + " with client ID " + client.getClientId());
        IMqttToken conToken = client.connect(conOpt, null, null);
        conToken.waitForCompletion();
        log.info("Connected");

        // Subscribe to the requested topic.
        // Control is returned as soon client has accepted to deliver the subscription.
        // Use a token to wait until the subscription is in place.
        log.info("Subscribing to topic \"" + topicName + "\" qos " + qos);

        IMqttToken subToken = client.subscribe(topicName, qos, null, null);
        subToken.waitForCompletion();
        log.info("Subscribed to topic \"" + topicName);

        // Continue waiting for messages until the Enter is pressed
        log.info("Waiting for messages");

        try {
            System.in.read();
        } catch (IOException e) {
            //If we can't read we'll just exit
        }

        // Disconnect the client
        // Issue the disconnect and then use the token to wait until
        // the disconnect completes.
        log.info("Disconnecting");
        IMqttToken discToken = client.disconnect(null, null);
        discToken.waitForCompletion();
        log.info("Disconnected");
    }


}
