package de.berlin.htw.s0558606.iotdatavisualizer.internal;

/**
 * Created by marcel on 20.11.17.
 */

public class Graph {

    private String graphName = "";
    private String graphTopic = "";
    private String yAxisDescription = "";

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
