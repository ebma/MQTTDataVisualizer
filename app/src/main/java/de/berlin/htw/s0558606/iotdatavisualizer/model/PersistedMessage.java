package de.berlin.htw.s0558606.iotdatavisualizer.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PersistedMessage {

    // Final Constant for DateFormat
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-DD HH:mm:ss";

    private final String topic;
    private final String message;
    private final Date timestamp;

    private static SimpleDateFormat formatter = new SimpleDateFormat();

    public PersistedMessage(String topic, String message, String timestamp) {
        this.topic = topic;
        this.message = message;
        this.timestamp = getTimestampAsDate(timestamp);
    }

    public PersistedMessage(String topic, String message, Date timestamp) {
        this.topic = topic;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static PersistedMessage convertToPersistedMessage(ReceivedMessage message){
        String stringMessage = new String(message.getMessage().getPayload());

        PersistedMessage persistedMessage = new PersistedMessage(message.getTopic(), stringMessage, message.getTimestamp());

        return persistedMessage;
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

    public static String getTimestampAsString(Date timestamp){
        formatter.applyPattern(PersistedMessage.TIMESTAMP_FORMAT);

        String timestampString = formatter.format(timestamp);

        return timestampString;
    }

    public static Date getTimestampAsDate(String timestamp){
        formatter.applyPattern(PersistedMessage.TIMESTAMP_FORMAT);

        Date result = null;
        try {
            result = formatter.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
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
