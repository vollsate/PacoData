package no.glv.paco.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import no.glv.paco.intrfc.Group;

/**
 * Handles all SQL query concerning <code>Group</code>
 */
class GroupTbl {

    private static Logger log = Logger.getLogger( GroupTbl.class.getSimpleName() );

    public static final String TBL_NAME = "group";

    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_YEAR = "year";

    private static final String SQL_CRATE_TABLE = "CREATE TABLE " + TBL_NAME + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT NOT NULL, " +
            COL_YEAR + " TEXT)";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME;

    private GroupTbl() {
    }

    /**
     *
     */
    public static boolean CreateTable( Statement db ) throws SQLException {
        String sql = SQL_CRATE_TABLE;

        log.finest( "Executing SQL: " + sql );
        boolean result = db.execute( sql );
        db.close();

        return result;
    }

    /**
     *
     */
    public static boolean DropTable( Statement db ) throws SQLException {
        String sql = SQL_DROP_TABLE;

        log.finest( "Executing SQL: " + sql );
        boolean result = db.execute( sql );

        db.close();
        return result;
    }

    /**
     * @return List of all known Group instances
     */
    public static List<Group> LoadAllGroups( Statement db ) throws SQLException {
        List<Group> list = new ArrayList<Group>();

        String sql = "SELECT * FROM " + TBL_NAME;
        log.finest( "Executing SQL: " + sql );

        ResultSet cursor = db.executeQuery( sql );

        while ( cursor.next() ) {
            list.add( CreateFromResultSet( cursor ) );
        }

        cursor.close();
        db.close();
        return list;
    }

    /**
     * @return A new Group object from the database
     */
    private static Group CreateFromResultSet( ResultSet cursor ) throws SQLException {
        GroupBean bean = new GroupBean( cursor.getInt( 0 ) );

        bean.setName( cursor.getString( 1 ) );
        bean.setName( cursor.getString( 2 ) );

        return bean;
    }

    /**
     *
     */
    public static void InsertGroup( Group group, Statement db ) throws SQLException {
        String sql = "INSERT INTO " + TBL_NAME +
                "(" + COL_NAME + ", " + COL_YEAR + ") " +
                "VALUES ('" + group.getName() + "', " +
                "'" + group.getYear() + "')";

        int retVal = db.executeUpdate( sql );
        log.finest( "Retval from InsertGroup: " + retVal );

        db.close();
    }

    /**
     * @return The number of rows deleted
     */
    public static int Delete( String name, Statement db ) throws SQLException {
        String sql = "DELETE from " + TBL_NAME + "WHERE " + COL_NAME + "=" + name;

        return db.executeUpdate( sql );
    }

}
