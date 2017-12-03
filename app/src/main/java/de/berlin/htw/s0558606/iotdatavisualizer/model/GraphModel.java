package de.berlin.htw.s0558606.iotdatavisualizer.model;

import java.util.Date;

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

    private Date graphMinX;
    private Date graphMaxX;

    private double graphMinY;
    private double graphMaxY;


    public GraphModel(){

    }

    /** Initialise the GraphModel with an existing graph **/
    public GraphModel(Graph graph){
        graphName = graph.getGraphName();
        graphTopic = graph.getGraphTopic();
        yAxisDescription = graph.getyAxisDescription();

        graphMinX = graph.getMinX();
        graphMaxX = graph.getMaxX();
        graphMinY = graph.getMinY();
        graphMaxY = graph.getMaxY();
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

    public Date getGraphMinX() {
        return graphMinX;
    }

    public void setGraphMinX(Date graphMinX) {
        this.graphMinX = graphMinX;
    }

    public Date getGraphMaxX() {
        return graphMaxX;
    }

    public void setGraphMaxX(Date graphMaxX) {
        this.graphMaxX = graphMaxX;
    }

    public double getGraphMinY() {
        return graphMinY;
    }

    public void setGraphMinY(double graphMinY) {
        this.graphMinY = graphMinY;
    }

    public double getGraphMaxY() {
        return graphMaxY;
    }

    public void setGraphMaxY(double graphMaxY) {
        this.graphMaxY = graphMaxY;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphModel that = (GraphModel) o;

        if (Double.compare(that.graphMinY, graphMinY) != 0) return false;
        if (Double.compare(that.graphMaxY, graphMaxY) != 0) return false;
        if (!graphName.equals(that.graphName)) return false;
        if (!graphTopic.equals(that.graphTopic)) return false;
        if (!yAxisDescription.equals(that.yAxisDescription)) return false;
        if (graphMinX != null ? !graphMinX.equals(that.graphMinX) : that.graphMinX != null)
            return false;
        return graphMaxX != null ? graphMaxX.equals(that.graphMaxX) : that.graphMaxX == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = graphName.hashCode();
        result = 31 * result + graphTopic.hashCode();
        result = 31 * result + yAxisDescription.hashCode();
        result = 31 * result + (graphMinX != null ? graphMinX.hashCode() : 0);
        result = 31 * result + (graphMaxX != null ? graphMaxX.hashCode() : 0);
        temp = Double.doubleToLongBits(graphMinY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(graphMaxY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
