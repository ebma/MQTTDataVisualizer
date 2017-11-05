package de.berlin.htw.s0558606.iotdatavisualizer.internal;

import de.berlin.htw.s0558606.iotdatavisualizer.model.ReceivedMessage;

public interface IReceivedMessageListener {

    void onMessageReceived(ReceivedMessage message);
}