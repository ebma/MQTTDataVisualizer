package de.berlin.htw.s0558606.iotdatavisualizer.model;

import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;

/**
 * Created by Marcel Ebert S0558606 on 20.11.17.
 */

public class GraphModel {

    private static final String DEFAULT_GRAPH_NAME = "Graph";
    private static final String DEFAULT_GRAPH_TOPIC = "/iotdata";
    private static final String DEFAULT_Y_AXIS_DESCRIPTION = "Temperature";


    private String graphName = "";
    private String graphTopic = "";
    private String yAxisDescription = "";


    public GraphModel(){

    }

    /** Initialise the GraphModel with an existing connection **/
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphModel that = (GraphModel) o;

        if (!graphName.equals(that.graphName)) return false;
        if (!graphTopic.equals(that.graphTopic)) return false;
        return yAxisDescription.equals(that.yAxisDescription);
    }

    @Override
    public int hashCode() {
        int result = graphName.hashCode();
        result = 31 * result + graphTopic.hashCode();
        result = 31 * result + yAxisDescription.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GraphModel{" +
                "graphName='" + graphName + '\'' +
                ", graphTopic='" + graphTopic + '\'' +
                ", yAxisDescription='" + yAxisDescription + '\'' +
                '}';
    }

    public static String getDefaultGraphName() {
        return DEFAULT_GRAPH_NAME;
    }

    public static String getDefaultGraphTopic() {
        return DEFAULT_GRAPH_TOPIC;
    }

    public static String getDefaultYAxisDescription() {
        return DEFAULT_Y_AXIS_DESCRIPTION;
    }
}
