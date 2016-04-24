package no.glv.paco.gsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import no.glv.paco.intrfc.Setting;

class SettingTbl  {

	private static final String TAG = SettingTbl.class.getSimpleName();

	public static final String TBL_NAME = "setting";

	// String (student ident)
	public static final String COL_ID = "_id";
	public static final int COL_ID_ID = 0;

	public static final String COL_NAME = "name";
	public static final int COL_NAME_ID = 1;

	public static final String COL_VALUE = "lname";
	public static final int COL_VALUE_ID = 2;

	private SettingTbl() {
	}

	/**
	 * Called as part of initiation of the entire DATABASE.
	 * 
	 * DO NOT CLOSE THE SQLiteDatabase
	 * 
	 * @param db
	 *            Do not close!
	 */
	static void CreateTableSQL( Statement db ) throws SQLException {
		String sql = "CREATE TABLE " + TBL_NAME + "("
				+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_NAME + " TEXT, "
				+ COL_VALUE + " TEXT)";

		db.execute( sql );
	}

	public static void DropTable( Statement db ) throws SQLException {
		String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

		db.executeUpdate( sql );
	}

	/**
	 * 
	 * @param db
	 * @return
	 */
	public static List<Setting> Load( int id, PreparedStatement db ) throws SQLException  {
		List<Setting> list = new ArrayList<Setting>();

		String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_ID + " = ?";
        db.setInt( 1, id );
		ResultSet cursor = db.executeQuery( sql );
		while ( cursor.next() ) {
			list.add( CreateFromCursor( cursor ) );
		}

		cursor.close();
		db.close();

		return list;
	}

	private static Setting CreateFromCursor( ResultSet cursor ) {
		Setting setting = null;

		return setting;
	}

	/**
     * TODO: Implement
	 */
	public static long Insert( Setting setting, PreparedStatement db ) throws SQLException {
		String sql = "";
        SettingValues( setting, db, 1 );

		long retVal = db.executeUpdate( sql );

		db.close();

		return retVal;
	}

	/**
	 * 
	 * @param setting
	 * @param db
	 *            Is closed after use
	 * 
	 * @return 1 if successful, 0 otherwise
	 */
	public static int Update( Setting setting, PreparedStatement db ) throws  SQLException {
		String sqlFiler = COL_ID + " = ?";
		SettingValues( setting, db, 2 );

		int retVal = db.executeUpdate( sqlFiler );
		db.close();

		return retVal;
	}

	/**
	 * 
	 * @param id
	 * @param db
	 */
	public static int Delete( int id, PreparedStatement db ) throws SQLException {
		String sqlFilter = COL_ID + " = ?";
		int retVal = db.executeUpdate( sqlFilter );
		db.close();

		return retVal;
	}

	/**
	 * 
	 * @param setting
	 * @return
	 */
	private static void SettingValues( Setting setting, PreparedStatement db, int index ) throws SQLException {
		db.setInt( index++, setting.getID() );
		db.setString( index++, setting.getName() );
		db.setString( index++, setting.getValue() );
	}
}
