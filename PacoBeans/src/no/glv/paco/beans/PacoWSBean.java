package no.glv.paco.beans;

/**
 * Represents the common interface to all the data classes used in the Web Service part of the PaCO application.
 * 
 * @author glevoll
 *
 */
public interface PacoWSBean {
    
    /**
     * Keeps track of the owner of the record stored in the database.
     * 
     * @return
     */
    String getUser();
    
    /**
     * Keeps track of the date the data was stored. 
     * 
     * <p>
     * May be used to delete and keep the database free from lots of old data.
     * 
     * @return The date the data was stored in the database.
     */
    long getDate();
}
