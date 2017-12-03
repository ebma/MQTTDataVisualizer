package de.berlin.htw.s0558606.iotdatavisualizer.internal;

import android.os.Parcel;
import android.os.Parcelable;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.berlin.htw.s0558606.iotdatavisualizer.model.PersistedMessage;

/**
 * Created by Marcel Ebert S0558606 on 20.11.17.
 */

/**
 * Contains attributes for graphviews to display data graphically
 */
public class Graph implements Parcelable {

    /**
     * GraphPersistence id, used by {@link GraphPersistence}
     **/
    private long persistenceId = -1;

    private String graphName = "";
    private String graphTopic = "";
    private String yAxisDescription = "";

    private Date minX, maxX;
    private double minY, maxY;

    private LineGraphSeries<DataPoint> lineGraphSeries;

    public Graph(String graphName, String graphTopic, String yAxisDescription) {
        this.graphName = graphName;
        this.graphTopic = graphTopic;
        this.yAxisDescription = yAxisDescription;
    }

    protected Graph(Parcel in) {
        persistenceId = in.readLong();
        graphName = in.readString();
        graphTopic = in.readString();
        yAxisDescription = in.readString();

        final ClassLoader cl = getClass().getClassLoader();

        minX = (Date) in.readValue(cl);
        maxX = (Date) in.readValue(cl);
        minY = in.readDouble();
        maxY = in.readDouble();
    }

    public static final Creator<Graph> CREATOR = new Creator<Graph>() {
        @Override
        public Graph createFromParcel(Parcel in) {
            return new Graph(in);
        }

        @Override
        public Graph[] newArray(int size) {
            return new Graph[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(persistenceId);
        dest.writeString(graphName);
        dest.writeString(graphTopic);
        dest.writeString(yAxisDescription);

        dest.writeValue(minX);
        dest.writeValue(maxX);
        dest.writeDouble(minY);
        dest.writeDouble(maxY);

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

    public Date getMinX() {
        return minX;
    }

    public void setMinX(Date minX) {
        this.minX = minX;
    }

    public Date getMaxX() {
        return maxX;
    }

    public void setMaxX(Date maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public LineGraphSeries<DataPoint> getLineGraphSeries() {
        return lineGraphSeries;
    }

    public void setLineGraphSeries(LineGraphSeries<DataPoint> lineGraphSeries) {
        this.lineGraphSeries = lineGraphSeries;
    }

    public long getPersistenceId() {
        return persistenceId;
    }

    public void setPersistenceId(long persistenceId) {
        this.persistenceId = persistenceId;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "persistenceId=" + persistenceId +
                ", graphName='" + graphName + '\'' +
                ", graphTopic='" + graphTopic + '\'' +
                ", yAxisDescription='" + yAxisDescription + '\'' +
                ", minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", lineGraphSeries=" + lineGraphSeries +
                '}';
    }
}
