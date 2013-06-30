package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.Calendar;
import java.util.List;

import android.content.SharedPreferences;
import android.database.SQLException;

/**
 * This class acts as the Model for the Application.
 * Using its methods is possible to perform all the basic CRUD operations
 * on the Day, Photo, Entry and Mood saved in the database.
 * 
 * @version 0.1 
 * @author groupENIGMA
 */
public interface DBInterface {

    /**
     * Open the connection to the database. You MUST call this method
     * before calling any other method that interacts with the database.
     * Should be called in the onResume() method of the Activity.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException;
    
    /**
     * Closes all the remaining active connections to the database.
     * You should call this method in the onPause() method of the Activity
     */
    public void close();

    /**
     * Checks if the database is open
     *
     * @return true if the database is open, false otherwise
     */
    public boolean isOpen();

    /**
     * Returns the Day object with date equal to the current system date
     * 
     * @return The Day object for today or null if it's not saved in the database
     * @throws DatabaseError when an error occurs parsing a date
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Day getDay();
    
    /**
     * Returns the Day object for the given date
     * 
     *
     * @param date the Calendar with year, month and day set to the wanted date
     * @return If available the Day of date, otherwise null
     * @throws DatabaseError when an error occurs parsing a date
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Day getDay(Calendar date);

    /**
     * Returns the diary's Day with the given id.
     * 
     *
     * @param id The searched id.
     * @return the Day with the given id or null (if the an Day with the
     * given id doesn't exists).
     * @throws DatabaseError when an error occurs parsing a date
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Day getDay(long id);

    /**
     * Creates a Day with the date set to today and inserts it into the database
     *
     * @return the newly created Day
     * @throws InvalidOperationException if a Day for today already exists.
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Day createDay();

    /**
     * Creates the Day with the given date and inserts it into the database
     *
     * @return the newly created Day
     * @throws InvalidOperationException if a Day for date already exists.
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Day createDay(Calendar date);
    
    /**
     * Deletes from the database the given Day
     *
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void deleteDay(Day day);

    /**
     * Checks if there's already a saved Day with today's date
     *
     * @return true if a Day for today already exists, false otherwise
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public boolean existsDay();

    /**
     * Checks if there's already a saved Day with the given date
     *
     *
     * @param date the date to check
     * @return true if a Day for date already exists, false otherwise
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public boolean existsDay(Calendar date);

    /** Returns the list of dates with a Day saved in the database
     * <p>
     * @return the list of dates in the diary, can be empty on a brand new
     *         database
     * @throws DatabaseError when an error occurs parsing a date
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public List<Calendar> getDatesList();
    
    /** Add a new Entry to the given Day.
     * <p>
     * It's possible to add an Entry to a Day only during the date of Day.
     * 
     *
     * @param day The new Entry will be added to this Day
     * @param note The text of the Entry
     * @param mood The Mood of the Entry (can be null)
     * @return the newly created Entry
     * @throws InvalidOperationException when adding an Entry to a Day other
     *         than today
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Entry insertEntry(Day day, String note, Mood mood);
    
    /**
     * Gets the Entry with the given id
     * 
     *
     * @param id the identifier of the Entry
     * @return The Entry with the specified id, or null if the an Entry with
     *         the given id doesn't exists.
     * @throws DatabaseError when an error occurs parsing a date
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Entry getEntry(long id);
    
    /**
     * Updates the Note of the given Entry and its database copy
     * <p>
     * Each Entry has a "grace period" during which it can be updated: calling
     * this method on a Entry after the end of the grace period will result
     * in an error.
     *
     * @param entry the Entry you want to update
     * @param new_note_text the new text of the note
     * @throws InvalidOperationException When note's grace period is already
     *         ended
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void setEntryNote(Entry entry, String new_note_text);

    /**
     * Updates the Mood of the given Entry and its database copy
     * <p>
     * Each Entry has a "grace period" during which it can be updated: calling
     * this method on a Entry after the end of the grace period will result
     * in an error.
     *
     * @param entry the Entry you want to update
     * @param new_mood the new Mood. If you want to remove the Mood of entry
     *        you can use null.
     * @throws InvalidOperationException When note's grace period is already
     *         ended
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void setEntryMood(Entry entry, Mood new_mood);
    
    /**
     * Deletes the given Entry from the database
     *
     * @param entry the Entry to be deleted
     * @param sharedPreferences The sharedPreferences file where the timeout
     *        for the Entry is saved
     * @throws InvalidOperationException If the Entry can't be deleted
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void deleteEntry(Entry entry, SharedPreferences sharedPreferences);
    
    /** Returns the list of available Moods.
     * <p>
     * The user can choose to assign to the entries one of these Moods.
     * 
     * @return the list of available Moods
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public List<Mood> getAvailableMoods();
    
    /**
     * Sets the Photo for the given Day
     *
     * @param day the Day to be updated
     * @param tmpPhotoPath the absolute path to the photo that will used
     *        for day. The file available at this path will be MOVED to the
     *        final photo location.
     * @return the newly created Photo object
     *
     * @throws InvalidOperationException if the Day's photo can't be inserted
     * @throws DatabaseError if unable to save the photo to ExternalStorage
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Photo setDayPhoto(Day day, String tmpPhotoPath);
    
    /**
     * Deletes the photo of the given day from db and external storage
     * 
     * A Photo can be deleted only during the same day it was took.
     * 
     * @param day
     * @throws InvalidOperationException When deleting a Photo that can't be
     *         deleted.
     * @throws DatabaseError When an error occurs saving the Photo file to
     *         ExternalStorage
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void removePhoto(Day day);
    
    /**
     * Returns the list of all the Photo saved in the database.
     * 
     * @return the List with all the Photo
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public List<Photo> getPhotos();
    
    /**
     * Return the list of all the Photo taken in the given time range
     * 
     * @param from the starting time of the range
     * @param to the ending time of the range
     * @return the List (can be empty) of matching Photo
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public List<Photo> getPhotos(Calendar from, Calendar to);

}
