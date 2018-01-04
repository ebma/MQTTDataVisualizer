package de.berlin.htw.s0558606.mqttdatavisualizer.activity;

public class ActivityConstants {

    /** Bundle key for passing a connection around by it's name **/
    public static final String CONNECTION_KEY = "CONNECTION_KEY";

    public static final String AUTO_CONNECT = "AUTO_CONNECT";
    public static final String CONNECTED = "CONNECTEd";

    public static final String LOGGING_KEY = "LOGGING_ENABLED";


    /**
     * Bundle key used for passing a graph object
     */
    public static final String GRAPH_KEY = "GRAPH_KEY";


    /** Property name for the history field in {@link Connection} object for use with {@link java.beans.PropertyChangeEvent} **/
    public static final String historyProperty = "history";

    /** Property name for the connection status field in {@link Connection} object for use with {@link java.beans.PropertyChangeEvent} **/
    public static final String ConnectionStatusProperty = "connectionStatus";



    /** Empty String for comparisons **/
    static final String empty = "";

}
