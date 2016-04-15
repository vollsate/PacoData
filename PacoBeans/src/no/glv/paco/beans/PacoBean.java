/**
 * 
 */
package no.glv.paco.beans;

/**
 * @author glevoll
 *
 */
public abstract class PacoBean implements PacoWSBean {
    
    private String user;
    
    private long date;
    
    public PacoBean(String user) {
        this( user, System.currentTimeMillis() );
    }
    
    public PacoBean(String user, long date) {
        this.user = user;
        this.date = date;
    }

    /* (non-Javadoc)
     * @see no.glv.paco.beans.PacoWSBean#getUser()
     */
    @Override
    public final String getUser() {
        return user;
    }

    /* (non-Javadoc)
     * @see no.glv.paco.beans.PacoWSBean#getDate()
     */
    @Override
    public final long getDate() {
        return date;
    }

}
