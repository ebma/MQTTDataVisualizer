package de.berlin.htw.s0558606.iotdatavisualizer.activity.graph;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.berlin.htw.s0558606.iotdatavisualizer.R;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.ActivityConstants;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.components.GraphListViewAdapter;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Connections;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.IReceivedMessageListener;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.PersistenceException;
import de.berlin.htw.s0558606.iotdatavisualizer.model.PersistedMessage;
import de.berlin.htw.s0558606.iotdatavisualizer.model.ReceivedMessage;

public class GraphFragment extends Fragment {

    private final int MAX_DATA_POINTS = 100;

    public static final String GRAPH_VIEW_PATTERN = "HH:mm:ss";

    private ArrayList<ReceivedMessage> messages;

    private List<Graph> graphList;

    private Connection connection;

    private ListView listView;
    private GraphListViewAdapter graphListViewAdapter;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        graphList = new ArrayList<>();

        Map<String, Connection> connections = Connections.getInstance(this.getActivity())
                .getConnections();
        connection = connections.get(this.getArguments().getString(ActivityConstants.CONNECTION_KEY));
        System.out.println("Graph Fragment: " + connection.getId());
        setHasOptionsMenu(true);
        messages = connection.getMessages();
        connection.addReceivedMessageListener(new IReceivedMessageListener() {
            @Override
            public void onMessageReceived(ReceivedMessage message) {
                updateGraph(message);
            }
        });

        try {
            graphList = connection.getGraphPersistence().restoreGraphs();
            if (graphList.size() > 0) {
                for (Graph graph : graphList) {
                    addSeriesToGraph(graph);
                }
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        //GraphView graphView = (GraphView) rootView.findViewById(R.id.graph);


        listView = rootView.findViewById(R.id.graph_list_view);
        // create listviewadapter
        graphListViewAdapter = new GraphListViewAdapter(this, graphList, connection);
        listView.setAdapter(graphListViewAdapter);

        if (graphList.size() > 0) {
            for (Graph graph : graphList) {
                GraphView graphView = new GraphView(rootView.getContext());

                graphView.addSeries(graph.getLineGraphSeries());

            }
            graphListViewAdapter.notifyDataSetChanged();
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateGraph(ReceivedMessage message) {
        for (Graph graph : graphList) {
            if (graph.getGraphTopic().equals(message.getTopic())) {
                PersistedMessage pMessage = PersistedMessage.convertToPersistedMessage(message);
                double value = Double.parseDouble(pMessage.getMessage());

                graph.getLineGraphSeries().appendData(new DataPoint(pMessage.getTimestamp(), value), true, Graph.MAX_DATA_POINTS);
            }
        }
        graphListViewAdapter.notifyDataSetChanged();
    }

    public void addSeriesToGraph(Graph graph) {
        graph.setLineGraphSeries(getDataPointsFromPersistedMessagesForTopic(graph.getGraphTopic()));
    }

    public LineGraphSeries<DataPoint> getDataPointsFromPersistedMessagesForTopic(String topic) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        List<PersistedMessage> messageList = null;
        try {
            messageList = connection.getMessagePersistence().restoreMessages(topic);
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (messageList != null) {
            for (PersistedMessage message : messageList) {
                try {
                    double value = Double.parseDouble(message.getMessage());
                    DataPoint dataPoint = new DataPoint(message.getTimestamp(), value);

                    series.appendData(dataPoint, true, MAX_DATA_POINTS);

                } catch (NumberFormatException e) {
                    System.out.println("Nachricht wurde uebersprungen: " + message.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return series;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_graph, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_graph) {
            AddGraphFragment addGraphFragment = new AddGraphFragment();

            Bundle bundle = new Bundle();
            bundle.putString(ActivityConstants.CONNECTION_KEY, connection.handle());
            addGraphFragment.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, addGraphFragment);
            fragmentTransaction.addToBackStack(addGraphFragment.getTag());
            fragmentTransaction.commit();
            return false;

        }

        return super.onOptionsItemSelected(item);
    }
}
