package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.Calendar;
import java.util.List;

/**
 * This class is used by {@link DBInterface} to model a Day of the diary.
 * <p>
 * You shouldn't create new instances of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DBInterface}
 * 
 * @version 0.1
 * @author groupENIGMA
 *
 */
public interface DayInterface {

    /**
     * Returns the day date
     * 
     * @return the Calendar with the Day date
     */
    public Calendar getDate();

    /**
     * Returns the unique id that identifies a Day
     * 
     * @return the id
     */
    public long getId();

    /**
     * Returns the Photo associated with the Day
     * 
     * @return the Photo if the Day has a Photo set
     *         otherwise return null
     */
    public Photo getPhoto();
    
    /**
     * Returns the List of all the entries associates with the Day
     * 
     * @return the List of Entries if at least one note is set,
     *         otherwise return an empty List
     */
    public List<Entry> getEntries();

    /**
     * Checks if the Day can be modified with a new Photo and/or Entry
     * 
     * @return true if the Day can be modified, false otherwise
     */
    public boolean canBeUpdated();
}
