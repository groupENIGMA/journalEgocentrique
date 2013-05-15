package journalEgocentrique.model;

import java.util.Calendar;

/**
 * This class is used by {@link DataSourceInterface} to model a daily
 * Entry of the diary.
 * <p>
 * You shouldn't create new instance of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DataSourceInterface}
 * 
 * @version 0.1
 * @author groupENIGMA
 *
 */
public interface EntryInterface {

    /**
     * Returns the day of the Entry
     * 
     * @return the day of the entry
     */
    public Calendar getDay();

    /**
     * Returns the unique id that identifies an Entry
     * 
     * @return the id
     */
    public long getId();

    /**
     * Checks if the Entry can be deleted 
     * 
     * @return true if the Entry can be deleted, false otherwise
     */
    public boolean canBeDeleted();

    /**
     * Checks if the Entry can be modified 
     * 
     * @return true if the Entry can be modified, false otherwise
     */
    public boolean canBeUpdated();
}
