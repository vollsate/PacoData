package no.glv.paco.gsql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import no.glv.paco.beans.StudentBean;
import no.glv.paco.intrfc.Student;

/**
 * Handles all SQL query about <code>Student</code>.
 */
class StudentTbl  {

    public static final String TBL_NAME = "student";

    public static final String COL_IDENT = "_ID";
    public static final int COL_IDENT_ID = 0;

    public static final String COL_CLASS = "class";
    public static final int COL_CLASS_ID = 1;

    public static final String COL_GRADE = "grade";
    public static final int COL_GRADE_ID = 2;

    public static final String COL_FNAME = "fname";
    public static final int COL_FNAME_ID = 3;

    public static final String COL_LNAME = "lname";
    public static final int COL_LNAME_ID = 4;

    public static final String COL_BIRTH = "birth";
    public static final int COL_BIRTH_ID = 5;

    public static final String COL_ADR = "adr";
    public static final int COL_ADR_ID = 6;

    public static final String COL_POSTALCODE = "pcode";
    public static final int COL_POSTALCODE_ID = 7;

    public static final String COL_PHONE = "phone";
    public static final int COL_PHONE_ID = 8;

    public static final String COL_STRENGTH = "strength";
    public static final int COL_STRENGTH_ID = 9;

    private StudentTbl() {
    }

    /**
     * Called as part of initiation of the entire DATABASE.

     */
    static void CreateTableSQL( Statement db ) throws SQLException {
        String sql = "CREATE TABLE " + TBL_NAME + "("
                + COL_IDENT + " TEXT PRIMARY KEY UNIQUE, "
                + COL_CLASS + " TEXT, "
                + COL_GRADE + " TEXT, "
                + COL_FNAME + " TEXT, "
                + COL_LNAME + " TEXT, "
                + COL_BIRTH + " TEXT, "
                + COL_ADR + " TEXT, "
                + COL_POSTALCODE + " TEXT, "
                + COL_PHONE + " TEXT, "
                + COL_STRENGTH + " TEXT)";

        db.executeUpdate( sql );
        db.close();
    }

    static void DropTable( Statement db ) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + TBL_NAME;

        db.executeUpdate( sql );
    }

    /**
     * @param group Loads every student registered to a particular class.
     * @param db       The database to query
     * @return List of every student registered
     */
    static List<Student> LoadStudentFromGroup(String group, PreparedStatement db ) throws SQLException {
        List<Student> list = new ArrayList<Student>();

        String sql = "SELECT * FROM " + TBL_NAME + " WHERE " + COL_CLASS + " = ?";
        db.setString( 1, group );

        ResultSet cursor = db.executeQuery( sql );
        while ( cursor.next() ) {
            list.add( CreateFromCursor( cursor ) );
        }

        cursor.close();
        db.close();

        return list;
    }

    private static Student CreateFromCursor( ResultSet cursor ) throws  SQLException {
        StudentBean bean = new StudentBean( null );

        bean.setIdent( cursor.getString( COL_IDENT_ID ) );
        bean.setGroupName( cursor.getString( COL_CLASS_ID ) );
        bean.setGrade( cursor.getString( COL_GRADE_ID ) );
        bean.setFirstName( cursor.getString( COL_FNAME_ID ) );
        bean.setLastName( cursor.getString( COL_LNAME_ID ) );
        bean.setBirth( DBUtils.ConvertStringToDate( cursor.getString( COL_BIRTH_ID ), null ) );
        bean.setAdress( cursor.getString( COL_ADR_ID ) );
        bean.setPostalCode( cursor.getString( COL_POSTALCODE_ID ) );
        bean.setPhone( cursor.getString( COL_PHONE_ID ) );
        bean.setStrength( cursor.getInt( COL_STRENGTH_ID ) );

        return bean;
    }

    /**
     * @param student The new student to insert
     * @param db      The database to update
     * @return The number of rows inserted
     */
    public static long Insert( Student student, PreparedStatement db ) throws SQLException {
        String sql = "";
        StudentValues( student, db, 1 );

        long retVal = db.executeUpdate( sql );
        db.close();

        return retVal;
    }

    /**
     * @param student The student to update
     * @param db      Is closed after use
     * @return 1 if successful, 0 otherwise
     *
     * TODO: Implement correctly
     */
    public static int Update( Student student, PreparedStatement db ) throws SQLException {
        String sqlFiler = COL_IDENT + " = ?";
        StudentValues( student, db, 2 );

        int retVal = db.executeUpdate( sqlFiler );
        db.close();

        return retVal;
    }

    /**
     * @param stdID The student ID to delete
     * @param db    The database to query
     * @return The number of rows deleted
     *
     * TODO: Implement correctly
     */
    public static int Delete( String stdID, PreparedStatement db ) throws SQLException {
        String sqlFilter = COL_IDENT + " = ?";
        int retVal = db.executeUpdate( sqlFilter );
        db.close();

        return retVal;
    }

    /**
     * @param student The student to convert
     * @return A <code>Student</code> converted to key/value pairs
     */
    private static void StudentValues( Student student, PreparedStatement db, int index ) throws SQLException {
        db.setString( index++, student.getIdent() );
        db.setString( index++, student.getGroupName() );
        db.setString(index++, student.getGrade() );
        db.setString(index++, student.getFirstName() );
        db.setString( index++, student.getLastName() );
        db.setString( index++, DBUtils.ConvertToString( student.getBirth() ) );
        db.setString( index++, student.getAdress() );
        db.setString( index++, student.getPostalCode() );
        db.setString( index++, student.getPhone() );
        db.setInt( index++, student.getStrength() );
    }
}
