package de.berlin.htw.s0558606.iotdatavisualizer.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Map;
import java.util.Random;

import de.berlin.htw.s0558606.iotdatavisualizer.R;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Connections;
import de.berlin.htw.s0558606.iotdatavisualizer.model.ConnectionModel;
import de.berlin.htw.s0558606.iotdatavisualizer.model.GraphModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGraphFragment extends Fragment {

    private EditText graphName;
    private EditText graphTopic;
    private EditText yAxisDescription;

    private GraphModel formModel;
    private boolean newConnection = true;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random random = new Random();
    private static final int length = 8;

    public AddGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_graph, container, false);
        graphName = (EditText) rootView.findViewById(R.id.graph_name);
        graphTopic = (EditText) rootView.findViewById(R.id.graph_topic);
        yAxisDescription = (EditText) rootView.findViewById(R.id.graph_y_axis);

        /*
        if (this.getArguments() != null && this.getArguments().getString(ActivityConstants.CONNECTION_KEY) != null) {
            /** This Form is referencing an existing connection. **/
            //this.getArguments().getString(ActivityConstants.CONNECTION_KEY)
            /*Map<String, Connection> connections = Connections.getInstance(this.getActivity())
                    .getConnections();
            String connectionKey = this.getArguments().getString(ActivityConstants.CONNECTION_KEY);
            Connection connection = connections.get(connectionKey);
            System.out.println("Editing an existing connection: " + connection.handle());
            newConnection = false;
            formModel = new GraphModel(graph);
            System.out.println("Form Model: " + formModel.toString());
            formModel.setClientHandle(connection.handle());

            populateFromConnectionModel(formModel);

        } else {
            formModel = new ConnectionModel();
            populateFromConnectionModel(formModel);

        }
        */
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
    private void populateFromConnectionModel(ConnectionModel connectionModel) {
        /*clientId.setText(connectionModel.getClientId());
        serverHostname.setText(connectionModel.getServerHostName());
        serverPort.setText(Integer.toString(connectionModel.getServerPort()));
        cleanSession.setChecked(connectionModel.isCleanSession());
        username.setText(connectionModel.getUsername());
        password.setText(connectionModel.getPassword());
        tlsServerKey.setText(connectionModel.getTlsServerKey());
        tlsClientKey.setText(connectionModel.getTlsClientKey());
        timeout.setText(Integer.toString(connectionModel.getTimeout()));
        keepAlive.setText(Integer.toString(connectionModel.getKeepAlive()));
        lwtTopic.setText(connectionModel.getLwtTopic());
        lwtMessage.setText(connectionModel.getLwtMessage());
        lwtQos.setSelection(connectionModel.getLwtQos());
        lwtRetain.setChecked(connectionModel.isLwtRetain());*/
    }

    private void saveConnection() {
        /*
        System.out.println("SAVING CONNECTION");
        System.out.println(formModel.toString());
        if (newConnection) {
            // Generate a new Client Handle
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(AB.charAt(random.nextInt(AB.length())));
            }
            String clientHandle = sb.toString() + '-' + formModel.getServerHostName() + '-' + formModel.getClientId();
            formModel.setClientHandle(clientHandle);
            ((MainActivity) getActivity()).persistAndConnect(formModel);

        } else {
            // Update an existing connection

            ((MainActivity) getActivity()).updateAndConnect(formModel);
        }*/


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
            ((MainActivity) getActivity()).onGraphSaved();
        }

        return super.onOptionsItemSelected(item);
    }

}
