package de.berlin.htw.s0558606.iotdatavisualizer.internal;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by marcel on 20.11.17.
 */

public class Graph {

    private String graphName = "";
    private String graphTopic = "";
    private String yAxisDescription = "";

    private LineGraphSeries<DataPoint> lineGraphSeries;

    public Graph(String graphName, String graphTopic, String yAxisDescription) {
        this.graphName = graphName;
        this.graphTopic = graphTopic;
        this.yAxisDescription = yAxisDescription;
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

    public LineGraphSeries<DataPoint> getLineGraphSeries() {
        return lineGraphSeries;
    }

    public void setLineGraphSeries(LineGraphSeries<DataPoint> lineGraphSeries) {
        this.lineGraphSeries = lineGraphSeries;
    }
}
