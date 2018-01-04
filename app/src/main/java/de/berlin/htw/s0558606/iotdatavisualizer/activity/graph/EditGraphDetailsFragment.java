package de.berlin.htw.s0558606.iotdatavisualizer.activity.graph;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Map;

import de.berlin.htw.s0558606.iotdatavisualizer.R;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.ActivityConstants;
import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Connections;
import de.berlin.htw.s0558606.iotdatavisualizer.internal.Graph;
import de.berlin.htw.s0558606.iotdatavisualizer.model.GraphModel;


public class EditGraphDetailsFragment extends Fragment {

    private Connection connection;
    private Graph graph;

    private ClickListener clickListener;

    private EditText graphName;
    private EditText graphTopic;
    private EditText yAxisDescription;

    // advanced category edittext views
    private EditText minXDate, minXTime;
    private EditText maxXDate, maxXTime;
    private EditText minY, maxY;

    private GraphModel formModel;

    // calendar used to save input as date
    private Calendar minXCalender, maxXCalendar;

    // Button used to delete text of advanced edittextviews
    private Button resetDefaultButton;

    public EditGraphDetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Map<String, Connection> connections = Connections.getInstance(this.getActivity()).getConnections();
        connection = connections.get(this.getArguments().getString(ActivityConstants.CONNECTION_KEY));

        graph = (Graph) this.getArguments().get(ActivityConstants.GRAPH_KEY);

        minXCalender = Calendar.getInstance();
        maxXCalendar = Calendar.getInstance();

        //TODO get connection
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_graph_details, container, false);
        graphName = rootView.findViewById(R.id.graph_name);
        graphTopic = rootView.findViewById(R.id.graph_topic);
        yAxisDescription = rootView.findViewById(R.id.graph_y_axis);

        minXDate = rootView.findViewById(R.id.graph_minx_datepicker);
        minXTime = rootView.findViewById(R.id.graph_minx_timepicker);
        maxXDate = rootView.findViewById(R.id.graph_maxx_datepicker);
        maxXTime = rootView.findViewById(R.id.graph_maxx_timepicker);

        minY = rootView.findViewById(R.id.graph_miny);
        maxY = rootView.findViewById(R.id.graph_maxy);
        minY.setSelectAllOnFocus(true);
        maxY.setSelectAllOnFocus(true);

        resetDefaultButton = rootView.findViewById(R.id.reset_default_graph_button);
        resetDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minXDate.setText("");
                minXTime.setText("");
                maxXDate.setText("");
                maxXTime.setText("");

                minY.setText("0");
                maxY.setText("0");
            }
        });

        // add clicklistener to edittextviews
        clickListener = new ClickListener();

        minXDate.setOnClickListener(clickListener);
        minXTime.setOnClickListener(clickListener);
        maxXDate.setOnClickListener(clickListener);
        maxXTime.setOnClickListener(clickListener);


        formModel = new GraphModel();

        setFormItemListeners();
        populateFromGraph(graph);




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

        minY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (s.length() == 1 && s.charAt(0) != '-') {
                        formModel.setGraphMinY(Double.parseDouble(s.toString()));
                    }
                }
            }
        });

        maxY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (s.length() == 1 && s.charAt(0) != '-') {
                        formModel.setGraphMaxY(Double.parseDouble(s.toString()));
                    }
                }
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void populateFromGraph(Graph graph) {
        graphName.setText(graph.getGraphName());
        graphTopic.setText(graph.getGraphTopic());
        yAxisDescription.setText(graph.getyAxisDescription());

        if (graph.getMinX() != null) {
            minXCalender.setTime(graph.getMinX());
            minXDate.setText(minXCalender.get(Calendar.DAY_OF_MONTH) + "-" + (minXCalender.get(Calendar.MONTH) + 1) + "-" + minXCalender.get(Calendar.YEAR));
            minXTime.setText(minXCalender.get(Calendar.HOUR_OF_DAY) + ":" + minXCalender.get(Calendar.MINUTE));
        }
        if (graph.getMaxX() != null) {
            maxXCalendar.setTime(graph.getMaxX());

            maxXDate.setText(maxXCalendar.get(Calendar.DAY_OF_MONTH) + "-" + (maxXCalendar.get(Calendar.MONTH) + 1) + "-" + maxXCalendar.get(Calendar.YEAR));
            maxXTime.setText(maxXCalendar.get(Calendar.HOUR_OF_DAY) + ":" + maxXCalendar.get(Calendar.MINUTE));
        }

        minY.setText(Double.toString(graph.getMinY()));
        maxY.setText(Double.toString(graph.getMaxY()));

    }

    private void saveGraph() {
        if (minXDate.getText().toString().length() > 0 && minXTime.getText().toString().length() > 0) {
            graph.setMinX(minXCalender.getTime());
        } else {
            graph.setMinX(null);
        }
        if (maxXDate.getText().toString().length() > 0 && maxXTime.getText().toString().length() > 0) {
            graph.setMaxX(maxXCalendar.getTime());
        } else {
            graph.setMaxX(null);
        }
        if (minY.getText().toString().length() > 0)
            graph.setMinY(Double.parseDouble(minY.getText().toString()));
        if (maxY.getText().toString().length() > 0)
            graph.setMaxY(Double.parseDouble(maxY.getText().toString()));

        graph.setGraphName(formModel.getGraphName().trim());
        graph.setGraphTopic(formModel.getGraphTopic().trim());
        graph.setyAxisDescription(formModel.getyAxisDescription().trim());

        connection.getGraphPersistence().updateGraph(graph);


        // switch back to connectionfragment to reload graphes
        DetailedGraphFragment fragment = new DetailedGraphFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ActivityConstants.CONNECTION_KEY, connection.handle());
        bundle.putParcelable(ActivityConstants.GRAPH_KEY, graph);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStackImmediate();
        //fragmentTransaction.replace(R.id.container_body, fragment);
        //fragmentTransaction.commit();
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


    /**
     * Clicklistener used to handle click events on minx and maxx edittext views
     * shows a DatePickerDialog or TimePickerDialog
      */
    class ClickListener implements View.OnClickListener {

        private final int DEFAULT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
        private final int DEFAULT_MONTH = Calendar.getInstance().get(Calendar.MONTH);
        private final int DEFAULT_DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        public ClickListener() {

        }


        @Override
        public void onClick(View view) {
            if (view == minXDate) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        minXDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        minXCalender.set(year, monthOfYear, dayOfMonth);

                    }
                }, DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY);
                // TODO change default values to currently input values
                datePickerDialog.show();
            } else if (view == minXTime) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int currentHour = c.get(Calendar.HOUR_OF_DAY);
                int currentMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                minXTime.setText(hourOfDay + ":" + minute);

                                minXCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                minXCalender.set(Calendar.MINUTE, minute);
                            }
                        }, currentHour, currentMinute, true);
                timePickerDialog.show();
            } else if (view == maxXDate) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        maxXDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        maxXCalendar.set(year, monthOfYear, dayOfMonth);

                    }
                }, DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY);
                datePickerDialog.show();
            } else if (view == maxXTime) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int currentHour = c.get(Calendar.HOUR_OF_DAY);
                int currentMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                maxXTime.setText(hourOfDay + ":" + minute);
                                maxXCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                maxXCalendar.set(Calendar.MINUTE, minute);
                            }
                        }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        }
    }

}
