package de.berlin.htw.s0558606.mqttdatavisualizer.internal;

import de.berlin.htw.s0558606.mqttdatavisualizer.model.ReceivedMessage;

public interface IReceivedMessageListener {

    void onMessageReceived(ReceivedMessage message);
}