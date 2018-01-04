package de.berlin.htw.s0558606.mqttdatavisualizer.activity.graph;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Map;
import java.util.Random;

import de.berlin.htw.s0558606.mqttdatavisualizer.R;
import de.berlin.htw.s0558606.mqttdatavisualizer.activity.ActivityConstants;
import de.berlin.htw.s0558606.mqttdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.mqttdatavisualizer.activity.ConnectionFragment;
import de.berlin.htw.s0558606.mqttdatavisualizer.internal.Connections;
import de.berlin.htw.s0558606.mqttdatavisualizer.internal.Graph;
import de.berlin.htw.s0558606.mqttdatavisualizer.internal.PersistenceException;
import de.berlin.htw.s0558606.mqttdatavisualizer.model.GraphModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGraphFragment extends Fragment {

    private EditText graphName;
    private EditText graphTopic;
    private EditText yAxisDescription;

    private GraphModel formModel;

    private Connection connection;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random random = new Random();
    private static final int length = 8;

    public AddGraphFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, Connection> connections = Connections.getInstance(this.getActivity()).getConnections();
        connection = connections.get(this.getArguments().getString(ActivityConstants.CONNECTION_KEY));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_graph, container, false);
        graphName = (EditText) rootView.findViewById(R.id.graph_name);
        graphTopic = (EditText) rootView.findViewById(R.id.graph_topic);
        yAxisDescription = (EditText) rootView.findViewById(R.id.graph_y_axis);

        graphName.setText(GraphModel.getDefaultGraphName());
        graphTopic.setText(GraphModel.getDefaultGraphTopic());
        yAxisDescription.setText(GraphModel.getDefaultYAxisDescription());

        formModel = new GraphModel();

        setFormItemListeners();


        // Inflate the layout for this fragment
        return rootView;
    }

    private void setFormItemListeners() {
        graphName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                formModel.setGraphName(s.toString());
            }
        });

        graphTopic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                formModel.setGraphTopic(s.toString());
            }
        });

        yAxisDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    formModel.setyAxisDescription(s.toString());
                }
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void populateFromGraphModel(GraphModel graphModel) {
        graphName.setText(graphModel.getGraphName());
        graphTopic.setText(graphModel.getGraphTopic());
        yAxisDescription.setText(graphModel.getyAxisDescription());
    }

    private void saveGraph() {
        Graph graph = new Graph(graphName.getText().toString(), graphTopic.getText().toString(), yAxisDescription.getText().toString());

        try {
            connection.getGraphPersistence().persistGraph(graph);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        // switch back to connectionfragment to reload graphes
        ConnectionFragment fragment = new ConnectionFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.CONNECTION_KEY, connection.handle());
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //fragmentManager.popBackStackImmediate();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_add_graph, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_graph) {
            saveGraph();
        }

        return super.onOptionsItemSelected(item);
    }

}
