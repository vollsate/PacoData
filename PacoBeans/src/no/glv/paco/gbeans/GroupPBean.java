/**
 * 
 */
package no.glv.paco.gbeans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import no.glv.paco.intrfc.Group;
import no.glv.paco.intrfc.Student;

/**
 * @author glevoll
 *
 */
public class GroupPBean extends PacoBean implements Group {
    
    private String name;
    
    private String year;
    
    private List<Student> students;
    
    public GroupPBean(String user) {
        super(user);
        
        init();
    }
    
    public GroupPBean(String user, long date) {
        super( user, date );
        
        init();
    }
    
    private void init() {
        students = new LinkedList<>();
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#setName(java.lang.String)
     */
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#getYear()
     */
    @Override
    public String getYear() {
        return year;
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#getSize()
     */
    @Override
    public int getSize() {
        return students.size();
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#getStudentByID(java.lang.String)
     */
    @Override
    public Student getStudentByID( String id ) {
        if ( id == null || id.length() == 0 ) return null;
        
        Iterator<Student> it = students.iterator();
        while ( it.hasNext() ) {
            Student st = it.next();
            if ( st.getIdent().equals( id ) ) return st;
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#add(no.glv.paco.intrfc.Student)
     */
    @Override
    public void add( Student std ) {
        if ( std == null ) return;
        
        students.add( std );
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#addAll(java.util.List)
     */
    @Override
    public void addAll( List<Student> list ) {
        if ( list == null || list.size() == 0 ) return;
        
        students.addAll( list );
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#iterator()
     */
    @Override
    public Iterator<Student> iterator() {
        return students.iterator();
    }

    /* (non-Javadoc)
     * @see no.glv.paco.intrfc.Group#getStudents()
     */
    @Override
    public List<Student> getStudents() {
        return students;
    }

}
