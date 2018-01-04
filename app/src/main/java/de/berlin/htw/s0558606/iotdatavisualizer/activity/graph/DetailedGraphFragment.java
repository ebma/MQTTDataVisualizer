package de.berlin.htw.s0558606.iotdatavisualizer.activity.graph;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import de.berlin.htw.s0558606.iotdatavisualizer.R;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.ActivityConstants;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Connections;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.IReceivedMessageListener;
import de.berlin.htw.s0558606.iotdatavisualizer.model.PersistedMessage;
import de.berlin.htw.s0558606.iotdatavisualizer.model.ReceivedMessage;

/**
 * Holds a graphview to display the graph in more detail
 * has an options menu where attributes of the graph object can be edited
 */
public class DetailedGraphFragment extends Fragment {

    private Connection connection;

    private Graph graph;
    private GraphView graphView;
    private IReceivedMessageListener listener;

    public DetailedGraphFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, Connection> connections = Connections.getInstance(this.getActivity()).getConnections();
        connection = connections.get(this.getArguments().getString(ActivityConstants.CONNECTION_KEY));
        graph = (Graph) this.getArguments().get(ActivityConstants.GRAPH_KEY);

        listener = new IReceivedMessageListener() {
            @Override
            public void onMessageReceived(ReceivedMessage message) {
                updateGraph(message);
            }
        };

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        connection.addReceivedMessageListener(listener);

    }

    @Override
    public void onPause() {
        super.onPause();
        connection.removeReceivedMessageListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_detailed_graph, container, false);

        graphView = rootView.findViewById(R.id.detailed_graphview);

        graphView.addSeries(graph.getLineGraphSeries());
        graphView.setTitle(graph.getGraphName());

        if (graph.getMinY() != graph.getMaxY()) {
            graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setMinY(graph.getMinY());
            graphView.getViewport().setMaxY(graph.getMaxY());
            System.out.println("YAXISBOUNDS MANUAL TRUE");
        }

        if (graph.getMinX() != null || graph.getMaxX() != null) {
            if (!graph.getMinX().equals(graph.getMaxX())) {
                graphView.getViewport().setXAxisBoundsManual(true);
                if (graph.getMinX() != null)
                    graphView.getViewport().setMinX(graph.getMinX().getTime());
                if (graph.getMaxX() != null)
                    graphView.getViewport().setMaxX(graph.getMaxX().getTime());
                System.out.println("XAXISBOUNDS MANUAL TRUE");
            }
        }


        graphView.getGridLabelRenderer().setVerticalAxisTitle(graph.getyAxisDescription());
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Date");

        // styling grid/labels
        graphView.getGridLabelRenderer().setHighlightZeroLines(true);
        graphView.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);
        graphView.getGridLabelRenderer().setLabelVerticalWidth(50);
        graphView.getGridLabelRenderer().setTextSize(30);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graphView.getGridLabelRenderer().setHorizontalLabelsAngle(120);
        graphView.getGridLabelRenderer().reloadStyles();

        graph.getLineGraphSeries().setDrawDataPoints(true);
        graph.getLineGraphSeries().setDataPointsRadius(10);
        graph.getLineGraphSeries().setThickness(8);


        // enable scrolling
        graphView.getViewport().setScrollable(true);

        // enable scaling
        graphView.getViewport().setScalable(true);

        // set date label formatter
        final SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(GraphFragment.GRAPH_VIEW_PATTERN);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphView.getContext(), dateFormat));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

        graph.getLineGraphSeries().setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                TextView textView = rootView.findViewById(R.id.detailed_graph_textview);
                dateFormat.applyPattern(GraphFragment.GRAPH_VIEW_EXTENDED_PATTERN);
                String date = dateFormat.format(new Date((long) dataPoint.getX()));
                textView.setText("X-Wert: " + date + System.getProperty("line.separator") + "Y-Wert: " + dataPoint.getY());
            }
        });

        return rootView;
    }

    public void updateGraph(ReceivedMessage message) {
        if (graph.getGraphTopic().equals(message.getTopic())) {
            PersistedMessage pMessage = PersistedMessage.convertToPersistedMessage(message);
            double value = pMessage.getValueFromMessage();

            graph.getLineGraphSeries().appendData(new DataPoint(pMessage.getTimestamp(), value), false, Graph.MAX_DATA_POINTS);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detailed_graph, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_graph) {
            EditGraphDetailsFragment fragment = new EditGraphDetailsFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(ActivityConstants.GRAPH_KEY, graph);
            bundle.putString(ActivityConstants.CONNECTION_KEY, connection.handle());

            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // add fragment to backstack so user returns when clicking backbutton
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}


