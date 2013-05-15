package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.Calendar;
import java.util.List;

/**
 * This class is used by {@link DBInterface} to model a daily
 * Entry of the diary.
 * <p>
 * You shouldn't create new instance of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DBInterface}
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
     * Returns the Mood associated with the Entry
     * 
     * @return the Mood if the Entry has a Mood set,
     *         otherwise return null
     */
    public Mood getMood();

    /**
     * Returns the Photo associated with the Entry
     * 
     * @return the Photo if the Entry has a Photo set
     *         otherwise return null
     */
    public Mood getPhoto();
    
    /**
     * Returns a List of all the notes associates with the Entry
     * 
     * @return the List of Notes if at least one note is set,
     *         otherwise return an empty List
     */
    public List<Note> getNotes();

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
