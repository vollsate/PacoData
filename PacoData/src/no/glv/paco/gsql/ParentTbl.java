package no.glv.paco.gsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import no.glv.paco.beans.ParentBean;
import no.glv.paco.intrfc.Parent;

/**
 * Handles all SQL queries about Parent
 */
class ParentTbl {

    private static Logger log = Logger.getLogger( ParentTbl.class.getSimpleName() );

    public static final String TBL_NAME = "parent";

    public static final String COL_ID = "_id";
    public static final int COL_ID_ID = 0;

    // String (student ident)
    public static final String COL_STDID = "stdid";
    public static final int COL_STDID_ID = 1;

    public static final String COL_FNAME = "fname";
    public static final int COL_FNAME_ID = 2;

    public static final String COL_LNAME = "lname";
    public static final int COL_LNAME_ID = 3;

    public static final String COL_MAIL = "mail";
    public static final int COL_MAIL_ID = 4;

    public static final String COL_TYPE = "type";
    public static final int COL_TYPE_ID = 5;

    private ParentTbl() {
    }

    /**
     * Called as part of initiation of the entire DATABASE.
     *
     * @param db Statement connection to database
     */
    static void CreateTableSQL( Statement db ) throws SQLException {
        String sql = "CREATE TABLE " + TBL_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_STDID + " TEXT NOT NULL, "
                + COL_FNAME + " TEXT, "
                + COL_LNAME + " TEXT, "
                + COL_MAIL + " TEXT, "
                + COL_TYPE + " INTEGER)";

        db.executeUpdate( sql );
        db.close();
    }

    /**
     * 
     * @param db
     * @throws SQLException
     */
    public static void DropTable( Statement db ) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

        log.finest( "Executing SQL: " + sql );
        db.executeUpdate( sql );
    }

    /**
     * Load the parent to a specific student ID. If studentID is null, loads
     * every
     * parent.
     *
     * @param studentID The student id to look for. May be null.
     * @param db The database to query
     * @return List of all parents connected to the student ID, or every known
     *         parent.
     */
    public static List<Parent> LoadParent( String studentID, Statement db ) throws SQLException {
        List<Parent> list = new ArrayList<Parent>();

        String sql = "SELECT * FROM " + TBL_NAME;
        if ( studentID != null )
            sql += " WHERE " + COL_STDID + " = " + studentID;

        ResultSet cursor = db.executeQuery( sql );
        while ( cursor.next() ) {
            list.add( CreateFromCursor( cursor ) );
        }

        cursor.close();
        db.close();

        return list;
    }

    private static Parent CreateFromCursor( ResultSet cursor ) throws SQLException {
        Parent parent = new ParentBean( cursor.getString( COL_ID_ID ), cursor.getInt( COL_TYPE_ID ) );

        parent.setFirstName( cursor.getString( COL_FNAME_ID ) );
        parent.setStudentID( cursor.getString( COL_STDID_ID ) );
        parent.setLastName( cursor.getString( COL_LNAME_ID ) );
        parent.setMail( cursor.getString( COL_MAIL_ID ) );

        return parent;
    }

    /**
     * Attempts to insert a parent into the database. If an error occurs, a
     * <code>DatabaseException</code> will be
     * thrown.
     *
     * @param parent The parent to insert
     * @param db The database to update
     * @return The number of rows updated
     */
    public static long InsertParent( Parent parent, Statement db ) throws SQLException {
        String sql = "INSERT INTO " + TBL_NAME + "VALUES( " +
                COL_FNAME + "=" + parent.getFirstName();

        long retVal = db.executeUpdate( sql );
        if ( retVal == -1 )
            throw new DBException( "Error inserting parent: " + parent.toString() );

        // Set the row number as the ID
        parent.setID( String.valueOf( retVal ) );
        db.close();

        return retVal;
    }

    /**
     * @param parent The parent to update
     * @param db Is closed after use
     * 
     * @return 1 if successful, 0 otherwise
     */
    public static int UpdateParent( Parent parent, PreparedStatement db ) throws SQLException {
        String sqlFiler = COL_STDID + " = ?";

        db.setString( 1, parent.getStudentID() );
        int retVal = db.executeUpdate( sqlFiler );
        db.close();

        return retVal;
    }

}
