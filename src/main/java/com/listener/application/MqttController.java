package com.listener.application;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
class MqttController implements MqttCallback {

    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        //to do
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
            log.info("Delivery complete callback: Publish Completed "+token.getMessage());
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




}
