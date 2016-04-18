package no.glv.paco.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.glv.paco.intrfc.Phone;

/**
 * Handles all SQL calls to the database concerning registered phone numbers.
 */
class PhoneTbl {

    private static Logger log = Logger.getLogger( PhoneTbl.class.getSimpleName() );

    public static final String TBL_NAME = "phone";

    public static final String COL_ID = "_id";
    public static final int COL_ID_ID = 0;

    /**
     * String (student ident)
     */
    public static final String COL_STDID = "stdid";
    public static final int COL_STDID_ID = 1;

    public static final String COL_PARENTID = "parentid";
    public static final int COL_PARENTID_ID = 2;

    /**
     * INTEGER
     */
    public static final String COL_PHONE = "phone";
    public static final int COL_PHONE_ID = 3;

    /**
     * INTEGER
     */
    public static final String COL_TYPE = "type";
    public static final int COL_TYPE_ID = 4;

    private PhoneTbl() {
    }

    /**
     * Called as part of initiation of the entire DATABASE.
     * <p>
     * DO NOT CLOSE THE SQLiteDatabase
     *
     * @param db Do not close!
     */
    static void CreateTable( Statement db ) throws SQLException {
        String sql = "CREATE TABLE " + TBL_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_STDID + " TEXT NOT NULL, "
                + COL_PARENTID + " TEXT, "
                + COL_PHONE + " LONG, "
                + COL_TYPE + " INTEGER)";

        log.finest( "Executing SQL: " + sql );
        db.executeUpdate( sql );
    }

    /**
     * Deletes the table from the database
     */
    public static void DropTable( Statement db ) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

        log.finest( "Executing SQL: " + sql );
        db.executeUpdate( sql );
    }

    /**
     * @param stdID The student ID
     * @param parentID The parent ID
     * @param db The database to lookup in
     * 
     * @return A list of all registered phone numbers
     */
    public static List<Phone> LoadParentPhone( String stdID, String parentID, PreparedStatement db ) throws SQLException {
        List<Phone> list = new ArrayList<Phone>();

        String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_STDID + " = ? AND " + COL_PARENTID + "=?";
        log.finest( "Executing SQL: " + sql );

        try {
            ResultSet cursor = db.executeQuery( sql );
            while ( cursor.next() ) {
                list.add( CreateFromCursor( cursor ) );
            }
            cursor.close();
        } catch ( Exception e ) {
            log.log(Level.WARNING, "Error getting phonerecord", e );
        }

        db.close();

        return list;
    }

    /**
     * Creates a new <code>Phone</code> object from the content of a Cursor
     * after a database query.
     */
    private static Phone CreateFromCursor( ResultSet cursor ) throws SQLException {
        Phone phone = new PhoneBean( cursor.getInt( COL_TYPE_ID ) );

        phone.setStudentID( cursor.getString( COL_STDID_ID ) );
        phone.setParentID( cursor.getString( COL_PARENTID_ID ) );
        phone.setNumber( cursor.getLong( COL_PHONE_ID ) );

        return phone;
    }

    /**
     * Inserts a phone number into the database
     *
     * @param phone The phone to insert
     * @param db Is closed after use
     */
    public static long Insert( Phone phone, PreparedStatement db ) throws SQLException {
        String sql = "INSERT INTO " + TBL_NAME + " VALUES(?, ?, ?, ?)";
        PhoneValues( phone, db );

        long retVal = db.executeUpdate( sql );
        db.close();

        return retVal;
    }

   /**
     * Will delete every phone registered to a single student ID
     *
     * @param stdID The student ID
     * @param db The database to lookup
     * @return The number of rows deleted
     */
    public static int Delete( String stdID, PreparedStatement db ) throws SQLException {
        String sql = "DELETE FROM + " + TBL_NAME + " WHERE " + COL_STDID + " = ?";
        db.setString( 1, stdID );

        int retVal = db.executeUpdate( sql );
        db.close();

        return retVal;
    }

    /**
     * Creates a key-value contentvalues object from a single Phone object.
     *
     * @param phone The phone to convert
     */
    private static void PhoneValues( Phone phone, PreparedStatement db ) throws  SQLException {
        PhoneValues( phone, db, 1);
    }

    /**
     *
     * @param phone
     * @param db
     * @param index
     * @throws SQLException
     */
    private static void PhoneValues( Phone phone, PreparedStatement db, int index ) throws  SQLException {
        db.setString( index++, phone.getStudentID() );
        db.setString( index++, phone.getParentID() );
        db.setLong( index++, phone.getNumber() );
        db.setInt( index++, phone.getType() );
    }
}
