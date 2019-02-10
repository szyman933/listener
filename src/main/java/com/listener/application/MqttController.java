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
    private MqttConnectOptions 	conOpt;
    private MqttAsyncClient 	client;

    public void messageArrived(String topic, MqttMessage message) throws MqttException {
       log.info("Otrzymano wiadomosc z tematu {} o tresci : {}",topic,message.toString());
    }
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Called when a message has been delivered to the
        // server. The token passed in here is the same one
        // that was passed to or returned from the original call to publish.
        // This allows applications to perform asynchronous
        // delivery without blocking until delivery completes.
        //
        // This sample demonstrates asynchronous deliver and
        // uses the token.waitForCompletion() call in the main thread which
        // blocks until the delivery has completed.
        // Additionally the deliveryComplete method will be called if
        // the callback is set on the client
        //
        // If the connection to the server breaks before delivery has completed
        // delivery of a message will complete after the client has re-connected.
        // The getPendinTokens method will provide tokens for any messages
        // that are still to be delivered.
        try {
            log.info("Delivery complete callback: Publish Completed");
        } catch (Exception ex) {
            log.info("Exception in delivery complete callback"+ex);
        }
    }

    public void connectionLost(Throwable cause) {
        // Called when the connection to the server has been lost.
        // An application may choose to implement reconnection
        // logic at this point. This sample simply exits.
        log.info("Connection to " + brokerUrl + " lost!" + cause);
        System.exit(1);
    }

    public MqttController(String brokerUrl, String clientId, boolean cleanSession,
                           boolean quietMode, String userName, String password) throws MqttException {
        this.brokerUrl = brokerUrl;
        this.clean 	   = cleanSession;
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
            if(password != null ) {
                conOpt.setPassword(this.password.toCharArray());
            }
            if(userName != null) {
                conOpt.setUserName(this.userName);
            }

            // Construct a non-blocking MQTT client instance
            client = new MqttAsyncClient(this.brokerUrl,clientId, dataStore);

            // Set this wrapper as the callback handler
            client.setCallback(this);

        } catch (MqttException e) {
            log.error(e.toString());
            log.info("Unable to set up client: "+e.toString());
            System.exit(1);
        }
    }


    public boolean publish(String topicName, int qos, byte[] payload) throws MqttException {

        // Connect to the MQTT server
        // issue a non-blocking connect and then use the token to wait until the
        // connect completes. An exception is thrown if connect fails.

        log.info("Connecting to "+brokerUrl + " with client ID "+client.getClientId());
        IMqttToken conToken = client.connect(conOpt,null,null);
        conToken.waitForCompletion();

        log.info("Connected");


        log.info("Publishing  to topic {} fallowing message : {}",topicName,payload.toString());

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

    public void subscribe(String topicName, int qos) throws MqttException {

        // Connect to the MQTT server
        // issue a non-blocking connect and then use the token to wait until the
        // connect completes. An exception is thrown if connect fails.
        log.info("Connecting to "+brokerUrl + " with client ID "+client.getClientId());
        IMqttToken conToken = client.connect(conOpt,null, null);
        conToken.waitForCompletion();
        log.info("Connected");

        // Subscribe to the requested topic.
        // Control is returned as soon client has accepted to deliver the subscription.
        // Use a token to wait until the subscription is in place.
        log.info("Subscribing to topic \""+topicName+"\" qos "+qos);

        IMqttToken subToken = client.subscribe(topicName, qos, null, null);
        subToken.waitForCompletion();
        log.info("Subscribed to topic \""+topicName);

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
