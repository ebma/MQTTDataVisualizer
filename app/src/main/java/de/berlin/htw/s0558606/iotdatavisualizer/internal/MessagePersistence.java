package de.berlin.htw.s0558606.iotdatavisualizer.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.berlin.htw.s0558606.iotdatavisualizer.activity.Connection;
import de.berlin.htw.s0558606.iotdatavisualizer.model.PersistedMessage;
import de.berlin.htw.s0558606.iotdatavisualizer.model.ReceivedMessage;
import de.berlin.htw.s0558606.iotdatavisualizer.model.Subscription;

public class MessagePersistence extends SQLiteOpenHelper implements BaseColumns {

    private static final String TAG = "MessagePersistence";

    /**
     * The version of the database
     **/
    private static final int DATABASE_VERSION = 1;

    /**
     * The name of the database file
     **/
    private String databaseName;

    /**
     * Table column for the last will message payload
     **/
    private static final String COLUMN_MESSAGE = "message";
    /**
     * Table column for timestamp of message
     **/
    private static final String COLUMN_TIMESTAMP = "timestamp";


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
     * Creates the persistence object passing it a context
     *
     * @param context Context that the application is running in
     */
    public MessagePersistence(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.databaseName = databaseName;
    }

    /* (non-Javadoc)
     * @see org.eclipse.paho.android.service.database.sqlite.SQLiteOpenHelper#onCreate(org.eclipse.paho.android.service.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO eigentlich soll hier nichts passieren, sondern durch extra methoden tabellen hinzugefuegtn werden
        // db.execSQL(SQL_CREATE_ENTRIES);

    }

    public void createTable(String tableName) {
        if (!exists(tableName)) {
            String tableQuery =
                    "CREATE TABLE " + tableName + " (" +
                            _ID + " INTEGER PRIMARY KEY," +
                            COLUMN_MESSAGE + TEXT_TYPE + COMMA_SEP +
                            COLUMN_TIMESTAMP + TEXT_TYPE + ");";

            getWritableDatabase().execSQL(tableQuery);
        }
    }

    public void deleteTable(String tableName) {
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    /**
     * Checks wheter a table exists
     *
     * @param table name of table
     * @return true if table exists
     */
    public boolean exists(String table) {
        try {
            getReadableDatabase().rawQuery("SELECT *", new String[]{table});
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.paho.android.service.database.sqlite.SQLiteOpenHelper#onUpgrade(org.eclipse.paho.android.service.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
        // db.execSQL(SQL_DELETE_ENTRIES);
        // db.execSQL(SQL_DELETE_SUBSCRIPTION_ENTRIES);
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
     * Persist a Connection to the database
     *
     * @param message the message to persist
     * @throws PersistenceException If storing the data fails
     */
    public void persistMessage(ReceivedMessage message) throws PersistenceException {
        SQLiteDatabase db = getWritableDatabase();

        //insert the values into the tables, returns the ID for the row
        long newRowId = db.insert(message.getTopic().replace("/", "_"), null, getValues(message));

        db.close(); //close the db then deal with the result of the query

        System.out.println("Message persisted: " + getValues(message));

        if (newRowId == -1) {
            throw new PersistenceException("Failed to persist connection: " + message.getMessage());
        } else { //Successfully persisted assigning persistenceID
            // TODO message.assignPersistenceId(newRowId);
        }
    }

    private ContentValues getValues(ReceivedMessage message) {
        PersistedMessage pMessage = PersistedMessage.convertToPersistedMessage(message);

        ContentValues values = new ContentValues();

        //put the column values object
        values.put(COLUMN_MESSAGE, pMessage.getMessage());
        values.put(COLUMN_TIMESTAMP, pMessage.getTimestampAsString(message.getTimestamp()));
        return values;
    }

    /**
     * Recreates connection objects based upon information stored in the database
     *
     * @return list of connections that have been restored
     * @throws PersistenceException if restoring connections fails, this is thrown
     */
    public List<PersistedMessage> restoreMessages(String topic) throws PersistenceException {

        //columns to return
        String[] messageColumns = {
                COLUMN_MESSAGE,
                COLUMN_TIMESTAMP,
                _ID

        };
        //how to sort the data being returned
        String sort = COLUMN_TIMESTAMP;

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(topic.replace("/", "_"), messageColumns, null, null, null, null, sort);
        ArrayList<PersistedMessage> list = new ArrayList<PersistedMessage>(c.getCount());
        for (int i = 0; i < c.getCount(); i++) {
            if (!c.moveToNext()) { //move to the next item throw persistence exception, if it fails
                throw new PersistenceException("Failed restoring connection - count: " + c.getCount() + "loop iteration: " + i);
            }
            //get data from cursor
            Long id = c.getLong(c.getColumnIndexOrThrow(_ID));

            //connect options strings
            String message = c.getString(c.getColumnIndexOrThrow(COLUMN_MESSAGE));
            String timeStamp = c.getString(c.getColumnIndexOrThrow(COLUMN_TIMESTAMP));

            PersistedMessage pMessage = new PersistedMessage(topic, message, timeStamp);

            //store it in the list
            list.add(pMessage);

        }
        //close the cursor now we are finished with it
        c.close();


        db.close();
        return list;

    }

}
