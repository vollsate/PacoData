package no.glv.paco.gsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.glv.paco.beans.TaskImpl;
import no.glv.paco.intrfc.Task;

/**
 * A static class that works towards a SQLite database. Takes care of loading,
 * inserting, deleting and updating tasks.
 *
 * @author GleVoll
 */
class TaskTbl {

    /**
     * Name of the TABLE in the SQLite database
     */
    public static final String TBL_NAME = "ntasks";

    public static final String COL_ID = "_ID";
    public static final int COL_ID_ID = 0;

    public static final String COL_NAME = "name";
    public static final int COL_NAME_ID = 1;

    public static final String COL_DESC = "desc";
    public static final int COL_DESC_ID = 2;

    public static final String COL_DATE = "date";
    public static final int COL_DATE_ID = 3;

    public static final String COL_STATE = "state";
    public static final int COL_STATE_ID = 4;

    public static final String COL_SUBJECT = "sbjct";
    public static final int COL_SUBJECT_ID = 5;

    public static final String COL_TYPE = "type";
    public static final int COL_TYPE_ID = 6;

    private TaskTbl() {
    }

    /**
     * @param db Is NOT closed after use!
     */
    public static void CreateTable( Statement db ) throws SQLException {
        String sql = "CREATE TABLE " + TBL_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_DESC + " TEXT, "
                + COL_DATE + " LONG NOT NULL, "
                + COL_STATE + " INTEGER, "
                + COL_SUBJECT + " INTEGER, "
                + COL_TYPE + " INTEGER)";
    }

    /**
     * @param db Is NOT closed after use!
     */
    public static void DropTable( Statement db ) throws SQLException {
        db.executeUpdate( "DROP TABLE IF EXISTS " + TBL_NAME );
    }

    /**
     * @return List of all tasks in the system
     */
    public static List<Task> loadAllTasks( Statement db ) throws SQLException {
        String sql = "SELECT * FROM " + TBL_NAME;
        List<Task> list = new ArrayList<Task>();
        ResultSet cursor = db.executeQuery( sql );

        while ( cursor.next() ) {
            list.add( CreateFromCursor( cursor ) );
        }

        cursor.close();
        db.close();
        return list;
    }

    /**
     * @param name Name of task to load
     * @return Task loaded of null if nothing found
     */
    static Task Load( String name, PreparedStatement db ) throws SQLException {
        String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_NAME + "=?";
        db.setString( 1, name );
        Task t = null;

        ResultSet c = db.executeQuery( sql );
        while ( c.next() ) {
            t = CreateFromCursor( c );
        }

        c.close();
        db.close();
        return t;
    }

    /**
     *
     */
    private static Task CreateFromCursor( ResultSet cursor ) throws SQLException {
        TaskImpl task = new TaskImpl( cursor.getInt( COL_ID_ID ) );

        task.setID( cursor.getInt( COL_ID_ID ) );
        task.setName( cursor.getString( COL_NAME_ID ) );
        task.setDescription( cursor.getString( COL_DESC_ID ) );
        task.setDate( new Date( cursor.getLong( COL_DATE_ID ) ) );
        task.setState( cursor.getInt( COL_STATE_ID ) );
        task.setSubject( cursor.getInt( COL_SUBJECT_ID ) );
        task.setType( cursor.getInt( COL_TYPE_ID ) );

        return task;
    }

    /**
     * @param task The task to insert
     * @param db   Is closed after use
     * @return Number of tasks inserted (should be 1 or 0)
     */
    public static int InsertTask( Task task, PreparedStatement db ) throws SQLException {
        String sql = "";

        TaksValues( task, db, 1 );
        int retVal = ( int ) db.executeUpdate( sql );

        db.close();
        return retVal;
    }

    /**
     *
     */
    private static void TaksValues( Task task, PreparedStatement db, int index ) throws SQLException {
        db.setString( index++, task.getName() );
        db.setString( index++, task.getDesciption() );
        db.setLong( index++, task.getDate().getTime() );
        db.setInt( index++, task.getState() );
        db.setInt( index++, task.getSubject() );
        db.setInt( index++, task.getType() );
    }

    /**
     * Updates a specific task
     *
     * @param task The {@link Task} to update, with all the new values
     * @param db   Is closed after use
     * @return 0 if no update, otherwise 1
     */
    public static int updateTask( Task task, PreparedStatement db ) throws SQLException {
        TaksValues( task, db, 2 );
        String whereClause = COL_ID + "=?";
        int retVal = db.executeUpdate( whereClause );

        db.close();

        return retVal;
    }

    /**
     * @param task The task to delete
     * @param db   Is closed after use
     * @return 1 if task deleted, 0 otherwise
     */
    public static int DeleteTask( Task task, PreparedStatement db ) throws SQLException {
        String whereClause = COL_ID + "=?";
        int retVal = db.executeUpdate( whereClause );

        db.close();
        return retVal;
    }

}
