package com.github.groupENIGMA.journalEgocentrique.model;

import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * This class is used by {@link DBInterface} to model a Note of
 * an Entry of the diary.
 * <p>
 * You shouldn't create new instance of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DBInterface}
 * 
 * @version 0.1
 * @author groupENIGMA
 *
 */
public interface NoteInterface {
    
    /** Returns the text comment of the Note.
     *
     * @return the text of the Note 
     */
    public String getText();
    
    /** Returns the unique id of the Note
     * 
     * @return the unique id of the Note
     */
    public long getId();

    /** Returns the date and time of when the Note was created
     *
     * @return the Calendar of when the Note was created
     */
    public Calendar getTime();

    /**
     * Checks if the Note can be updated.
     * <p>
     * A Note can be updated only during a grace period set by the final
     * user in the SharedPreferences
     * 
     * @return true if the Note can be updated, false otherwise
     */
    public boolean canBeUpdated(SharedPreferences preferences);
    
    /**
     * Checks if the Note can be deleted 
     * <p>
     * A Note can be deleted only during a grace period set by the final
     * user in the SharedPreferences
     * 
     * @return true if the Note can be deleted, false otherwise
     */
    public boolean canBeDeleted(SharedPreferences preferences);
    
}
