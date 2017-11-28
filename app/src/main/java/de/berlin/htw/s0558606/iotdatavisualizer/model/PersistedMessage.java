package de.berlin.htw.s0558606.iotdatavisualizer.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by marcel on 28.11.17.
 */

public class PersistedMessage {

    private final String topic;
    private final String message;
    private final Date timestamp;

    public PersistedMessage(String topic, String message, String timestamp) {
        this.topic = topic;
        this.message = message;

        String target = "Thu Sep 28 20:29:30 JST 2000";
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Date result = null;
        try {
            result = df.parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(result);


        this.timestamp = result;
    }


    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ReceivedMessage{" +
                "topic='" + topic + '\'' +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}';
    }
}
