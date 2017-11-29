package de.berlin.htw.s0558606.iotdatavisualizer.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;

/**
 * Created by Marcel Ebert S0558606 on 28.11.17.
 */

public class GraphPersistence extends SQLiteOpenHelper implements BaseColumns {

    private static final String TAG = "GraphPersistence";

    /**
     * The version of the database
     **/
    private static final int DATABASE_VERSION = 1;

    /**
     * The name of the database file
     **/
    private String databaseName;

    /**
     * The name of the table
     */
    private static final String TABLE_GRAPHS = "graphs";

    /**
     * Table column for the subscription-topic of the graph
     **/
    private static final String COLUMN_TOPIC = "topic";

    /**
     * Table column for the name of the graph
     **/
    private static final String COLUMN_NAME = "name";

    /**
     * Table column for description of graph
     **/
    private static final String COLUMN_DESCRIPTION = "description";


    //sql lite data types
    /**
     * Text type for SQLite
     **/
    private static final String TEXT_TYPE = " TEXT";
    /**
     * Int type for SQLite
     **/
    private static final String INT_TYPE = " INTEGER";
    /**
     * Comma separator
     **/
    private static final String COMMA_SEP = ",";

    /**
     * Create tables query
     **/
    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + TABLE_GRAPHS + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TOPIC + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_DESCRIPTION + TEXT_TYPE + ");";

    /**
     * Delete tables entry
     **/
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_GRAPHS;


    /**
     * Creates the persistence object passing it a context
     *
     * @param context Context that the application is running in
     * @param clientID
     */
    public GraphPersistence(Context context, String clientID) {
        super(context, clientID + "_graph.db", null, DATABASE_VERSION);
        this.databaseName = clientID + "_graph.db";
    }

    /* (non-Javadoc)
     * @see org.eclipse.paho.android.service.database.sqlite.SQLiteOpenHelper#onCreate(org.eclipse.paho.android.service.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    /* (non-Javadoc)
     * @see org.eclipse.paho.android.service.database.sqlite.SQLiteOpenHelper#onUpgrade(org.eclipse.paho.android.service.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.paho.android.service.database.sqlite.SQLiteOpenHelper#onDowngrade(org.eclipse.paho.android.service.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Persist a Graph to the database
     *
     * @param graph the graph to persist
     * @throws PersistenceException If storing the data fails
     */
    public void persistGraph(Graph graph) throws PersistenceException {
        SQLiteDatabase db = getWritableDatabase();

        //insert the values into the tables, returns the ID for the row
        long newRowId = db.insert(TABLE_GRAPHS, null, getValues(graph));

        db.close(); //close the db then deal with the result of the query

        System.out.println("Graph persisted: " + getValues(graph));

        if (newRowId == -1) {
            throw new PersistenceException("Failed to persist graph: " + graph.getGraphName());
        } else { //Successfully persisted assigning persistenceID

        }
    }

    private ContentValues getValues(Graph graph) {
        String name = graph.getGraphName();
        String topic = graph.getGraphTopic();
        String description = graph.getyAxisDescription();

        ContentValues values = new ContentValues();

        //put the column values object
        values.put(COLUMN_TOPIC, topic);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        return values;
    }

    /**
     * Recreates graph objects based upon information stored in the database
     *
     * @return list of graphs that have been restored
     * @throws PersistenceException if restoring connections fails, this is thrown
     */
    public List<Graph> restoreGraphs() throws PersistenceException {

        //columns to return
        String[] graphColumns = {
                COLUMN_TOPIC,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                _ID

        };
        //how to sort the data being returned
        String sort = COLUMN_TOPIC;

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(TABLE_GRAPHS, graphColumns, null, null, null, null, sort);
        ArrayList<Graph> list = new ArrayList<Graph>(c.getCount());
        for (int i = 0; i < c.getCount(); i++) {
            if (!c.moveToNext()) { //move to the next item throw persistence exception, if it fails
                throw new PersistenceException("Failed restoring graph - count: " + c.getCount() + "loop iteration: " + i);
            }
            //get data from cursor
            Long id = c.getLong(c.getColumnIndexOrThrow(_ID));

            //connect options strings
            String topic = c.getString(c.getColumnIndexOrThrow(COLUMN_TOPIC));
            String name = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME));
            String description = c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION));

            Graph graph = new Graph(name, topic, description);

            //store it in the list
            list.add(graph);

        }
        //close the cursor now we are finished with it
        c.close();


        db.close();
        return list;

    }
}
