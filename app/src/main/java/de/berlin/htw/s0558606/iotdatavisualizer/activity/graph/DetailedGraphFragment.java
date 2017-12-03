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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.SimpleDateFormat;
import java.util.Map;

import de.berlin.htw.s0558606.iotdatavisualizer.R;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.ActivityConstants;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Connections;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;

/**
 * Holds a graphview to display the graph in more detail
 * has an options menu where attributes of the graph object can be edited
 *
 */
public class DetailedGraphFragment extends Fragment {

    private Connection connection;

    private Graph graph;
    private GraphView graphView;

    public DetailedGraphFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, Connection> connections = Connections.getInstance(this.getActivity()).getConnections();
        connection = connections.get(this.getArguments().getString(ActivityConstants.CONNECTION_KEY));
        graph = (Graph) this.getArguments().get(ActivityConstants.GRAPH_KEY);


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detailed_graph, container, false);

        graphView = rootView.findViewById(R.id.detailed_graphview);

        graphView.addSeries(graph.getLineGraphSeries());
        graphView.setTitle(graph.getGraphName());

        if (graph.getMinY() != graph.getMaxY()) {
            graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setMinY(graph.getMinY());
            graphView.getViewport().setMaxY(graph.getMaxY());
        }

        if (graph.getMinX() != null && graph.getMaxX() != null) {
            if (!graph.getMinX().equals(graph.getMaxX())) {
                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMinX(graph.getMinX().getTime());
                graphView.getViewport().setMaxX(graph.getMaxX().getTime());
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
        //graphView.getViewport().setScrollable(true);

        // enable scaling
        //graphView.getViewport().setScalable(true);

        // TODO add ondatapointclicklistener and show detailed message attributes in view below graphview


        // set date label formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(GraphFragment.GRAPH_VIEW_PATTERN);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphView.getContext(), dateFormat));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

        return rootView;
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


