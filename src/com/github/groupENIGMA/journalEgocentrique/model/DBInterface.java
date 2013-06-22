package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.Calendar;
import java.util.List;

import android.database.SQLException;
import android.graphics.Bitmap;

/**
 * This class acts as the Model for the Application.
 * Using its methods is possible to perform all the basic CRUD operations
 * on the Entry, Photo and Note saved in the database.
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
     * Returns the diary's Entry for today.
     * 
     * @return The Entry for today or null if an Entry for today doesn't exists
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Entry getEntry();
    
    /**
     * Returns the diary's Entry for a given day.
     * 
     * @param day
     * @return The Entry for day or null if an Entry for day doesn't exists
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Entry getEntry(Calendar day);

    /**
     * Returns the diary's Entry with the given id.
     * 
     * @param id The searched id.
     * @return the Entry with the given id or null (if the an Entry with the
     * given id doesn't exists).
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Entry getEntry(long id);

    /**
     * Creates the Entry for today and inserts it into the database
     *
     * @return the newly created Entry
     * @throws InvalidOperationException if an Entry for today already exists.
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Entry createEntry();

    /**
     * Creates the Entry for the given day and inserts it into the database
     *
     * @return the newly created Entry
     * @throws InvalidOperationException if an Entry for day already exists.
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Entry createEntry(Calendar day);

    /**
     * Checks if there's already an Entry for the today in the database
     *
     * @return true if an Entry for today already exists, false otherwise
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public boolean existsEntry();

    /**
     * Checks if there's already an Entry for the day in the database
     *
     * @return true if an Entry for today already exists, false otherwise
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public boolean existsEntry(Calendar day);

    /** Returns the list of days that have an Entry associated
     * <p>
     * @return the list of days in the diary, can be empty on a brand new
     *         database
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public List<Calendar> getDays();
    
    /** Insert a new text Note to the given Entry.
     * <p>
     * It's possible to add a Note to an Entry only during the day of creation
     * of the note.
     * 
     * @param entry
     * @param note_text The text of the Note
     * @return the newly created Note
     * @throws InvalidOperationException When adding a Note to an Entry that
     *         is wasn't created today
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Note insertNote(Entry entry, String note_text);
    
    /**
     * Gets the Note with the given id
     * 
     * @param id the identifier of the Note
     * @return The Note with the specified id, or null if the a Note with the
     *         given id doesn't exists.
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Note getNote(long id);
    
    /**
     * Updates the text note of the given Note
     * <p>
     * Each Note has a "grace period" during which it can be updated: calling
     * this method on a Note after the end of the grace period will result
     * in an error.
     *
     * @param note the Node you want to update
     * @param new_note_text the new text of the note
     * @return the new version of the Note
     * @throws InvalidOperationException When note's grace period is already
     *         ended
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Note updateNote(Note note, String new_note_text);
    
    /**
     * Deletes the given Note from the database
     * 
     * @param note the Note to be deleted
     * @throws InvalidOperationException If the given Note isn't in the database or if 
     * the operation is not permitted
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void deleteNote(Note note);
    
    /**
     * Sets the mood for the given Entry
     * <p>
     * The Mood of an Entry can be modified only during the day the Entry was
     * created.
     * 
     * @param entry the Entry to update
     * @param mood the new Mood
     * @throws InvalidOperationException If the Entry's mood can't be modified
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void setMood(Entry entry, Mood mood);
    
    /**
     * Sets the Mood for given Entry to null
     * 
     * @param entry
     * @throws InvalidOperationException if the Entry's mood can't be removed
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void removeMood(Entry entry);
    
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
     * Sets the Photo for the given Entry
     * 
     * @param entry the Entry to be updated
     * @param btmp the Bitmap file of the taken picture
     * @return the newly created Photo object
     * 
     * @throws InvalidOperationException if the Entry's photo can't be inserted
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public Photo setPhoto(Entry entry, Bitmap btmp);
    
    /**
     * Deletes the given photo from the database
     * 
     * A Photo can be deleted only during the same day it was took.
     * 
     * @param photo the Photo to be deleted
     * @throws InvalidOperationException When deleting a Photo that can't be
     *         deleted.
     * @throws ConnectionException if called before connecting to the database
     *         with {@link DB#open()}.
     */
    public void deletePhoto(Photo photo) throws InvalidOperationException;
    
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
