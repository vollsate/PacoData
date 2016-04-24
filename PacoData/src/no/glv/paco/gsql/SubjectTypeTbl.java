package no.glv.paco.gsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import no.glv.paco.beans.SubjectTypeBean;
import no.glv.paco.intrfc.SubjectType;

/**
 * Represents a {@link SubjectType} instance in the database.
 * 
 * @author glevoll
 *
 */
class SubjectTypeTbl {

	private static final Logger log = Logger.getLogger( SubjectTypeTbl.class.getSimpleName() );

	public static final String TBL_NAME = "sbjcttp";

	public static final String COL_ID = "_id";
	public static final int COL_ID_ID = 0;

	/** String - name of subject/type */
	public static final String COL_NAME = "name";
	public static final int COL_NAME_ID = 1;

	/** String - description */
	public static final String COL_DESC = "desc";
	public static final int COL_DESC_ID = 2;

	/** INTEGER */
	public static final String COL_TYPE = "type";
	public static final int COL_TYPE_ID = 3;

	private SubjectTypeTbl() {
	}

	/**
	 * Called as part of initiation of the entire DATABASE.
	 * 
	 * DO NOT CLOSE THE SQLiteDatabase
	 * 
	 * @param db Do not close!
	 */
	static void CreateTable( Statement db ) throws SQLException {
		String sql = "CREATE TABLE " + TBL_NAME + "("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_NAME + " TEXT NOT NULL, "
				+ COL_DESC + " TEXT, "
				+ COL_TYPE + " INTEGER)";

		db.executeUpdate( sql );
	}


	public static void DropTable( Statement db ) throws SQLException {
		String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

		db.executeUpdate( sql );
	}

	/**
	 * Loads all known {@link SubjectType} found in the database.
	 * 
	 * @param db
	 * @return A list of every known {@link SubjectType} or an empty list if
	 *         something fails
	 */
	public static List<SubjectType> LoadAll( PreparedStatement db ) throws SQLException {
		List<SubjectType> list = new ArrayList<SubjectType>();

		String sql = "SELECT * FROM " + TBL_NAME;

		try {
			ResultSet cursor = db.executeQuery( sql );
			while ( !cursor.isAfterLast() ) {
				list.add( CreateFromCursor( cursor ) );
			}
			cursor.close();
		}
		catch ( Exception e ) {
			// TODO
		}

		db.close();

		return list;
	}

	/**
	 * Creates a {@link SubjectType} instance from an SQL cursor object.
	 * 
	 * <p>
	 * NB! The cursor will NOT be closed
	 * 
	 * @param cursor
	 * @return
	 */
	private static SubjectType CreateFromCursor( ResultSet cursor ) throws SQLException {
		SubjectTypeBean st = new SubjectTypeBean( cursor.getInt( COL_ID_ID ) );

		st.setName( cursor.getString( COL_NAME_ID ) );
		st.setDescription( cursor.getString( COL_DESC_ID ) );
		st.setType( cursor.getInt( COL_TYPE_ID ) );

		return st;
	}

	/**
	 * Inserts one {@link SubjectType} instance in the database.
	 * 
	 * <p>
	 * The row number will be the ID, and may be retrieved through the
	 * {@link SubjectType#getID()}.
	 * 
	 * @param st
	 * @param db The SQL database will be closed
	 * 
	 * @return The row number of the newly created {@link SubjectType}.
	 */
	public static long Insert( SubjectType st, PreparedStatement db ) throws SQLException {
		return Insert( st, db, true );
	}

	/**
	 * Inserts a list of {@link SubjectType} instances.
	 * 
	 * @param list
	 * @param db
	 * @return The number of rows inserted in the database.
	 */
	public static long Insert( List<SubjectType> list, PreparedStatement db ) throws SQLException {
		int count = 0;
		for ( SubjectType st : list ) {
			long retVal = Insert( st, db, false );
			if ( retVal > 0 )
				count++;
		}

		db.close();
		return count;
	}

	/**
	 * Insert one {@link SubjectType} instance. The rownumber will be set as the
	 * ID of the {@link SubjectType}.
	 * 
	 * @param st
	 * @param db
	 * @param close Weather or not to close the DB connection
	 * 
	 * @return The row number in the database.
     *
     * TODO: Implement
	 */
	private static long Insert( SubjectType st, PreparedStatement db, boolean close ) throws SQLException {
		String sql = "";
        SQLValues( st, db, 1 );

		// retVal will be row number or -1 if error
		long retVal = db.executeUpdate( sql );
		if ( retVal == -1 ) {
			// TODO Log.e( TAG, "Failed to insert SubjectType: " + st.getName() );
		}
		else
			( (SubjectTypeBean) st ).setId( (int) retVal );

		if ( close )
			db.close();

		return retVal;
	}

	/**
	 * 
	 * @param st
	 * @param db Is closed after use
	 * 
	 * @return 1 if successful, 0 otherwise
	 */
	public static int Update( SubjectType st, PreparedStatement db ) throws SQLException {
		String sqlFiler = COL_ID + " = ?";
		SQLValues( st, db, 2 );

		// retVal will be 1 if success, 0 otherwise
		int retVal = db.executeUpdate( sqlFiler);
		if ( retVal == 0 )
			log.warning( "Error updating SubjecType#name = " + st.getName() );
		
		db.close();

		return retVal;
	}

	/**
	 * 
	 * @param id The row number of the SubjectType
	 * @param db
	 */
	public static int Delete( int id, PreparedStatement db ) throws SQLException {
		String sqlFilter = COL_ID + " = ?";
		
		// retVal will be rows deleted, or 0
		int retVal = db.executeUpdate( sqlFilter );
		if ( retVal == 0 ) {
			log.warning( "Error deleting SubjectType#ID = " + id );
		}
		
		db.close();

		return retVal;
	}

	/**
	 * Creates a SQL insert object from a {@link SubjectType}.
	 * 
	 * @param st
	 * @return
	 */
	private static void SQLValues( SubjectType st, PreparedStatement db, int index ) throws SQLException {
		db.setString( index++, st.getName() );
		db.setString( index++, st.getDescription() );
		db.setInt( index++, st.getType() );
	}
}
