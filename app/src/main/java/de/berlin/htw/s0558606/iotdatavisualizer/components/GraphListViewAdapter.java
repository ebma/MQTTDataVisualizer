package de.berlin.htw.s0558606.iotdatavisualizer.components;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.SimpleDateFormat;
import java.util.List;

import de.berlin.htw.s0558606.iotdatavisualizer.R;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.ActivityConstants;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.graph.DetailedGraphFragment;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.graph.GraphFragment;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;

/**
 * Created by Marcel Ebert S0558606 on 29.11.17.
 */

public class GraphListViewAdapter extends ArrayAdapter<Graph> {

    private Fragment fragment;
    private List<Graph> graphs;

    private Connection connection;

    public GraphListViewAdapter(Fragment fragment,  List<Graph> graphs, Connection connection) {
        super(fragment.getContext(), R.layout.graph_list_item, graphs);
        this.fragment = fragment;
        this.graphs = graphs;
        this.connection = connection;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.graph_list_item, parent, false);
        GraphView graphView = view.findViewById(R.id.graphview);

        final Graph graph = graphs.get(position);
        graphView.addSeries(graph.getLineGraphSeries());
        graphView.setTitle(graph.getGraphName());

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
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(GraphFragment.GRAPH_VIEW_PATTERN);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphView.getContext(), dateFormat));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

        graphView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedGraphFragment detailedGraphFragment = new DetailedGraphFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelable(ActivityConstants.GRAPH_KEY, graph);
                bundle.putString(ActivityConstants.CONNECTION_KEY, connection.handle());

                detailedGraphFragment.setArguments(bundle);

                FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // add fragment to backstack so user returns when clicking backbutton
                fragmentTransaction.addToBackStack(detailedGraphFragment.getTag());
                fragmentTransaction.replace(R.id.container_body, detailedGraphFragment);
                fragmentTransaction.commit();
            }
        });

        graphView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                graphs.remove(position);
                connection.getGraphPersistence().deleteSubscription(graph);
                notifyDataSetChanged();

                return true;
            }
        });

        return graphView;
    }
}

