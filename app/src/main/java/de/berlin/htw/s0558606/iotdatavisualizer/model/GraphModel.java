package de.berlin.htw.s0558606.iotdatavisualizer.model;

import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;

/**
 * Created by marcel on 20.11.17.
 */

public class GraphModel {

    private static final String CLIENT_HANDLE = "CLIENT_HANDLE";
    private static final String CLIENT_ID = "CLIENT_ID";
    private static final String HOST_NAME = "HOST_NAME";
    private static final String PORT = "PORT";
    private static final String CLEAN_SESSION = "CLEAN_SESSION";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String TLS_SERVER_KEY = "TLS_SERVER_KEY";
    private static final String TLS_CLIENT_KEY = "TLS_CLIENT_KEY";
    private static final String TIMEOUT = "TIMEOUT";
    private static final String KEEP_ALIVE = "KEEP_ALIVE";
    private static final String LWT_TOPIC = "LWT_TOPIC";
    private static final String LWT_MESSAGE = "LWT_MESSAGE";
    private static final String LWT_QOS = "LWT_QOS";
    private static final String LWT_RETAIN = "LWT_RETAIN";


    private String graphName = "";
    private String graphTopic = "";
    private String yAxisDescription = "";


    public GraphModel(){

    }

    /** Initialise the ConnectionModel with an existing connection **/
    public GraphModel(Graph graph){
        graphName = graph.getGraphName();
        graphTopic = graph.getGraphTopic();
        yAxisDescription = graph.getyAxisDescription();
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public String getGraphTopic() {
        return graphTopic;
    }

    public void setGraphTopic(String graphTopic) {
        this.graphTopic = graphTopic;
    }

    public String getyAxisDescription() {
        return yAxisDescription;
    }

    public void setyAxisDescription(String yAxisDescription) {
        this.yAxisDescription = yAxisDescription;
    }
}
