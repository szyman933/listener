package com.listener.application;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.io.IOException;

@Slf4j
class MqttController implements MqttCallback {


    private UnitInputRepo unitInputRepo;

    private UnitRepo unitRepo;

    private UnitTypeRepo unitTypeRepo;

    private ReadingsRepo readingsRepo;

    private String broker;

    private String clientId;

    private boolean clean;


    private MqttConnectOptions conOpt;
    private MqttAsyncClient client;

    public void messageArrived(String topic, MqttMessage message) {


        log.info("Otrzymano wiadomosc z tematu {} o tresci : {}", topic, message.toString());

        String frame = new String(message.getPayload());

        HeaderParser headerParser = new HeaderParser(frame);

        if (headerParser.toProcess()) {

            switch (headerParser.getMessageType()) {


                case 0:
                    Installation installation = new Installation(unitRepo, unitTypeRepo, unitInputRepo);

                    installation.parse(frame, headerParser.getTransmitter());

                    installation.persist();
                    break;

                case 1:
                    ReadingsHandler readingsHandler = new ReadingsHandler(readingsRepo, unitInputRepo);

                    readingsHandler.parse(frame,headerParser.getTransmitter());

                    readingsHandler.persist();
                    break;
                default:
            }

        } else {
            log.info("Message is for other client, skipping");
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
        log.info("Connection to " + broker + " lost!" + cause);
        System.exit(1);
    }


    MqttController(UnitRepo ur, UnitTypeRepo utr, UnitInputRepo uir, ReadingsRepo rr, MqttConfig mqttConfig) {


        this.unitRepo = ur;
        this.unitTypeRepo = utr;
        this.unitInputRepo = uir;
        this.readingsRepo = rr;
        this.broker = mqttConfig.getBroker();
        this.clientId = mqttConfig.getClientid();
        this.clean = mqttConfig.isCleanSession();


        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

        try {

            conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(clean);

            // Construct a non-blocking MQTT client instance
            client = new MqttAsyncClient(broker, clientId, dataStore);

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
        log.info("Connecting to " + broker + " with client ID " + client.getClientId());
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
        log.info("Connecting to " + broker + " with client ID " + client.getClientId());
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
