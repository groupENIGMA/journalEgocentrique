package com.github.groupENIGMA.journalEgocentrique.model;

import android.content.SharedPreferences;
import java.util.Calendar;

/**
 * This class is used by {@link DBInterface} to model an Entry of the Diary.
 * <p>
 * You shouldn't create new instances of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DBInterface}
 * 
 * @version 0.1
 * @author groupENIGMA
 *
 */
public interface EntryInterface {

    /** Returns the unique id of the Entry
     *
     * @return the unique id of the Entry
     */
    public long getId();

    /** Returns the date and time of when the Entry was created
     *
     * @return the Calendar of when the Entry was created
     */
    public Calendar getTime();

    /** Returns the text note of the Entry.
     *
     * @return the text of the Entry
     */
    public String getNote();

    /** Returns the Mood of the Entry
     *
     * @return the Mood (null if not set)
     */
    public Mood getMood();

    /**
     * Checks if the Entry can be updated.
     * <p>
     * A Entry can be updated only during a grace period set by the final
     * user in the SharedPreferences
     *
     * @param preferences the shared preferences file where the Entry grace
     *        period is saved.
     * @return true if the Entry can be updated, false otherwise
     */
    public boolean canBeUpdated(SharedPreferences preferences);

}
