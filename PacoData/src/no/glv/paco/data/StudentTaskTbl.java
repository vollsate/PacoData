package no.glv.paco.data;

import java.awt.Cursor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import no.glv.paco.intrfc.Assignment;
import no.glv.paco.intrfc.Task;

class StudentTaskTbl {

	private static final String TAG = StudentTaskTbl.class.getSimpleName();

	public static final String TBL_NAME = "stdntsk";

	public static final String COL_ID = "_ID";
	public static final int COL_ID_ID = 0;

	public static final String COL_TASK = "task";
	public static final int COL_TASK_ID = 1;

	public static final String COL_IDENT = "ident";
	public static final int COL_IDENT_ID = 2;

	public static final String COL_DATE = "date";
	public static final int COL_DATE_ID = 3;

	/** Weather or not student has delivered (IN | PENDING | EXPIRED | LATE ) */
	public static final String COL_MODE = "mode";
	public static final int COL_MODE_ID = 4;

	public static final String COL_COMMENT = "comment";
	public static final int COL_COMMENT_ID = 5;

	/**
	 * 
	 */
	private StudentTaskTbl() {
	}

	/**
	 * 
	 * @param db
	 */
	public static void CreateTable( Statement db ) throws SQLException {
		String sql = "CREATE TABLE " + TBL_NAME + "("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_TASK + " INTEGER NOT NULL, "
				+ COL_IDENT + " TEXT NOT NULL, "
				+ COL_DATE + " LONG, "
				+ COL_MODE + " LONG NOT NULL, "
				+ COL_COMMENT + " TEXT)";

		db.executeUpdate( sql );
	}

	/**
	 * 
	 * @param db
	 */
	public static void DropTable( Statement db ) throws SQLException {
		String sql = "DROP TABLE IF EXISTS " + TBL_NAME;
		db.executeUpdate( sql );
	}

	/**
	 * Loads all the unique task names stored in the database. 
	 * 
	 * <p>Used to keep track of the database.
	 * 
	 * @param db
	 * @return
	 */
	public static List<String> FindAllTaskNames( Statement db ) throws SQLException {
		List<String> list = new LinkedList<String>();
		String sql = "SELECT distinct " + COL_TASK + " FROM " + TBL_NAME;

		ResultSet cursor = db.executeQuery( sql );

		while ( cursor.next() ) {
			list.add( cursor.getString( 0 ) );
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 
	 * @param taskID
	 * @param db
	 * @return
	 */
	public static int RemoveStudentsInTask( int taskID, PreparedStatement db ) throws SQLException {
		String sql = "DELETE FROM " + TBL_NAME +
				" WHERE " + COL_TASK + "=?";

		db.setInt( 1, taskID );
		int row = db.executeUpdate( sql );

		db.close();
		return row;
	}

	/**
	 * 
	 * @param db
	 * @return
	 */
	public static List<Assignment> LoadAllInTask( PreparedStatement db, Task task ) throws SQLException {
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_TASK + "=?";
		db.setInt( 1, task.getID() );

		ResultSet cursor = db.executeQuery( sql );
		List<Assignment> list = new ArrayList<Assignment>();

		while ( cursor.next() ) {
			list.add( CreateFromCursor( cursor ) );
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 
	 * @param db
	 * @return
	 */
	public static List<Assignment> LoadAll( Statement db ) throws SQLException {
		String sql = "SELECT * FROM " + TBL_NAME;

		ResultSet cursor = db.executeQuery( sql );
		List<Assignment> list = new ArrayList<Assignment>();

		while ( cursor.next() ) {
			list.add( CreateFromCursor( cursor ) );
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 
	 * @param ident
	 * @param task
	 * @param db
	 * @return
	 */
	public static Assignment Load( String ident, String task, PreparedStatement db ) throws  SQLException {
		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_TASK + "=? AND " + COL_IDENT + "=?";
		db.setString( 1, task);
		db.setString( 2, ident );

		ResultSet cursor = db.executeQuery( sql );

/*
		if ( cursor.getMetaData(). > 1 )
			throw new IllegalStateException( "Too many Assignment: ident=" + ident
					+ ", task=" + task );
*/
		return CreateFromCursor( cursor );
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private static Assignment CreateFromCursor( ResultSet cursor ) throws SQLException {
		int id = cursor.getInt( COL_ID_ID );
		int task = cursor.getInt( COL_TASK_ID );
		String ident = cursor.getString( COL_IDENT_ID );
		String comment = cursor.getString( COL_COMMENT_ID );
		
		long dateL = cursor.getLong( COL_DATE_ID );
		int mode = cursor.getInt( COL_MODE_ID );
		Date date = null;
		if ( dateL > 0 )
			date = new Date( dateL );

		AssignmentBean impl = new AssignmentBean( ident, task, mode, date );
		impl.setID( id );
		impl.setComment( comment );

		return impl;
	}

	/**
	 * 
	 * @param task
	 * @param db
	 * @return
	 */
	public static int Insert( Assignment task, PreparedStatement db ) throws SQLException {
		int retVal = InsertOneST( task, db );
		db.close();
		return retVal;
	}

	/**
	 * 
	 * @param task
	 * @param db Does NOT close the connection
	 * @return
	 */
	private static int InsertOneST( Assignment task, PreparedStatement db ) throws SQLException {
        String sql = "";

		long rowNum = db.executeUpdate( sql );

		( (AssignmentBean )task).setID( (int) rowNum );
		return (int) rowNum;
	}

	/**
	 * 
	 * @param task
	 * @param db
	 * @return
	 */
	public static void InsertAll( Task task, PreparedStatement db ) throws SQLException {
		Iterator<Assignment> it = task.getAssignmentList().iterator();
		while ( it.hasNext() ) {
			Assignment st = it.next();
			st.setTaskName( task.getName() );
			st.setTaskID( task.getID() );
			InsertOneST( st, db );
		}

		db.close();
	}

	/**
	 * 
	 * @param stdTask
	 * @param db
	 * @return
	 */
	public static long Update( Assignment stdTask, PreparedStatement db ) throws SQLException {
		long retVal = 0;

		String sql = COL_ID + "=?";
		retVal = db.executeUpdate( sql );

		return retVal;
	}

	/**
	 * 
	 * @param stdTask
	 * @param db
	 * @return
	 */
	public static int Delete( Assignment stdTask, PreparedStatement db ) throws SQLException {
		int retVal = 0;

		String sql = COL_ID + "=?";
		retVal = db.executeUpdate( sql );

		return retVal;
	}

	/**
	 * 
	 * @param task
	 * @return
	 */
	private static void StudentTaskValues( Assignment task, PreparedStatement db, int index ) throws SQLException {
		db.setInt( index++, task.getTaskID() );
		db.setString( index++, task.getIdent() );

		Date date = task.getHandInDate();
		long dateL = 0;
		if ( date != null )
			dateL = date.getTime();
		db.setLong( index++, dateL );

		db.setInt( index++, task.getMode() );
		db.setString( index++, task.getComment() );
	}

}
