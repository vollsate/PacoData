/**
 *
 */
package no.glv.paco.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.glv.paco.intrfc.Group;
import no.glv.paco.intrfc.Student;

/**
 * Represents a group (class) of students.
 *
 * @author GleVoll
 *
 */
public class GroupBean implements Group {

    /** Name of the group */
    private String name;

    /** The row ID in the database */
    private int _id;

    /** The year when this group is active */
    private String year;

    /** All students linked to this group */
    private ArrayList<Student> students;

    /**
     * @param id The row ID from the database
     */
    public GroupBean( int id ) {
        students = new ArrayList<Student>();

        this._id = id;
    }

    /*
     * (non-Javadoc)
     *
     * @see no.glv.paco.intrfc.Group#getName
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * (non-Javadoc)
     *
     * @see Group#getSize()
     */
    @Override
    public int getSize() {
        return students.size();
    }

    /**
     *
     * @param name
     * @return
     */
    public Student getStudentByID( String name ) {
        Student bean = null;
        Iterator<Student> it = students.iterator();

        while ( it.hasNext() ) {
            bean = it.next();
            if ( bean.getIdent().equals( name ) )
                break;
            else
                bean = null;
        }

        return bean;
    }

    @Override
    public void add( Student std ) {
        students.add( std );
    }

    @Override
    public void addAll( List<Student> list ) {
        students.addAll( list );
    }

    @Override
    public Iterator<Student> iterator() {
        return students.iterator();
    }

    @Override
    public List<Student> getStudents() {
        return students;
    }

    public int get_id() {
        return _id;
    }

    public String getYear() {
        return year;
    }

    public void setYear( String year ) {
        this.year = year;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
