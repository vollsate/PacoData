package no.glv.paco.gsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.glv.paco.intrfc.Assignment;
import no.glv.paco.intrfc.Group;
import no.glv.paco.intrfc.Student;
import no.glv.paco.intrfc.Task;

/**
 * The main entry point into the database. This will keep only one instance, and
 * throw an exception is somebody tries to instantiate the database more than
 * once.
 *
 * @author glevoll
 */
public class CloudSQLDatabase {

    private static Logger log = Logger.getLogger( CloudSQLDatabase.class.getSimpleName() );

    static {
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch ( ClassNotFoundException ex ) {
            log.warning( "Can not load JDBC Driver: " + ex.getMessage() );
        }
    }

    /**
     * The current version of the database used.
     */
    public static final int DB_VERSION = 1;

    /**
     * The name of the database
     */
    public static final String DB_NAME = "pacu";

    private String dbAddress;
    private String dbUser;
    private String dbPwd;

    /**
     * The singleton instance used to prevent more than one instance
     */
    private static CloudSQLDatabase instance;

    /** The database connection */
    private Connection _con;

    /**
     * @return The singleton instance
     * 
     * @throws DBException if unable to load the JDBC driver
     */
    public static CloudSQLDatabase GetInstance( String dbAddress, String dbUser, String dbPassword ) {
        if ( instance == null ) {
            try {
                Class.forName( "com.mysql.jdbc.Driver" );
            } catch ( ClassNotFoundException ex ) {
                log.warning( "Can not load JDBC Driver: " + ex.getMessage() );
                throw new DBException( ex );
            }

            instance = new CloudSQLDatabase( dbAddress, dbUser, dbPassword );

        }

        return instance;
    }

    /**
     * Creates the database content provider.
     *
     * @throws DBException is database already instantiated
     */
    public CloudSQLDatabase( String dbAddress, String dbUser, String dbPassword ) throws DBException {
        if ( instance != null )
            throw new IllegalStateException();

        this.dbAddress = dbAddress;
        this.dbUser = dbUser;
        this.dbPwd = dbPassword;

        try {
            _con = DriverManager.getConnection( dbAddress, dbUser, dbPassword );
        } catch ( SQLException sqlEx ) {
            log.warning( "Cannot get connection to database: " + sqlEx.getMessage() );
            throw new DBException( "Unable to connect to database: " + dbAddress, sqlEx );
        }
    }


    public void initDatabase() throws SQLException {
        GroupTbl.CreateTable( _con.createStatement() );
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    //
    // STUDENT
    //
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    //
    // TASK
    //
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    //
    // GROUP
    //
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return List of all registered <code>Group</code>
     */
    public List<Group> loadAllGroups() throws DBException {
        try {
            return GroupTbl.LoadAllGroups( _con.createStatement() );
        } catch ( Exception e ) {
            log.log( Level.WARNING, "Cannot load groups", e );
            throw new DBException( "Cannot load groups", e );
        }

    }

    /**
     * Will insert a Group and all students connected to it. If no students are
     * connected (size=0) an error will be
     * thrown.
     *
     * @param group The group to insert
     *
     * @throws DBException if group is empty
     */
    public void insertGroup( Group group ) throws SQLException {
        if ( group.getSize() == 0 )
            throw new DBException( "Cannot insert empty group!" );

        GroupTbl.InsertGroup( group, _con.createStatement() );

        List<Student> list = group.getStudents();
        for ( Student std : list ) {
            std.setGroupName( group.getName() );
            // insertStudent( std );
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    //
    // STUDENT IN TASK
    //
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param task The task to find students connected to
     *
     * @return List of all students connected to the specified task
     */
    public List<Assignment> loadStudentsInTask( Task task ) {
        return null;
    }

    /**
     * @return A list of all known studentTask instances
     */
    public List<Assignment> loadAllStudentTask() {
        return null;
    }

}
