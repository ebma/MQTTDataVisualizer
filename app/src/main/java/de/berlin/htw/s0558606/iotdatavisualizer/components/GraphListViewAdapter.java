package de.berlin.htw.s0558606.iotdatavisualizer.components;

import android.content.Context;
import android.content.PeriodicSync;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.berlin.htw.s0558606.iotdatavisualizer.R;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.GraphFragment;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;
import de.berlin.htw.s0558606.iotdatavisualizer.model.PersistedMessage;
import de.berlin.htw.s0558606.iotdatavisualizer.model.ReceivedMessage;

/**
 * Created by Marcel Ebert S0558606 on 29.11.17.
 */

public class GraphListViewAdapter extends ArrayAdapter<Graph> {

    private final Context context;
    private List<Graph> graphs;

    public GraphListViewAdapter(Context context, List<Graph> graphs) {
        super(context, R.layout.graph_list_item, graphs);
        this.context = context;
        this.graphs = graphs;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.graph_list_item, parent, false);
        GraphView graphView = view.findViewById(R.id.graphview);

        Graph graph = graphs.get(position);
        graphView.addSeries(graph.getLineGraphSeries());
        graphView.setTitle(graph.getGraphName());


        // set manual X bounds
        //graphView.getViewport().setXAxisBoundsManual(true);
        //graphView.getViewport().setMinX(0);
        //graphView.getViewport().setMaxX(10);

        graphView.getGridLabelRenderer().setVerticalAxisTitle(graph.getyAxisDescription());
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Date");

        // enable scrolling
        graphView.getViewport().setScrollable(true);


        // set date label formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(GraphFragment.GRAPH_VIEW_PATTERN);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphView.getContext(), dateFormat));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

        return graphView;
    }
}

