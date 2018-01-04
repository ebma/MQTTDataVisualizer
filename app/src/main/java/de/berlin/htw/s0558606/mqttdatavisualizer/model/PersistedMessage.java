package de.berlin.htw.s0558606.mqttdatavisualizer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used for Messages that are restored from database
 * in contrast to ReceivedMessage the timestamp is saved as string object
 */
public class PersistedMessage {

    // Final Constant for DateFormat
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-DD HH:mm:ss";
    private static SimpleDateFormat formatter = new SimpleDateFormat();

    private final String topic;
    private final String message;
    private final Date timestamp;


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

    /**
     * Converts an Object of Type ReceivedMessage to an Object of type PersistedMessage
     * The difference is the timestamp parameter
     * @param message the receivedmessage to be converted
     * @return an object of type persistedmessage
     */
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


    public double getValueFromMessage(){
        double value = 0;
        if (getMessage().split(" ").length > 1){
            value = Double.parseDouble(getMessage().split(" ")[1]);
        } else if (getMessage().split(" ").length == 1) {
            value = Double.parseDouble(getMessage());
        } else {
            System.err.println("getValueFromMessage() failed ");
        }

        return value;
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
