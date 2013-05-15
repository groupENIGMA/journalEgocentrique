package journalEgocentrique.model;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import android.database.SQLException;

/**
 * This class acts as the Model for the Application.
 * Using its methods is possible to perform all the basic CRUD operations
 * on the Entry, Photo and Note saved in the database.
 * 
 * @version 0.1 
 * @author groupENIGMA
 */
public interface DataSourceInterface {

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
     * Returns the diary's Entry for today.
     * <p>
     * Only one Entry per day can exists so, if the database already has the
     * Entry for today, the existing one will be returned. Otherwise a new
     * Entry will be created, inserted in the database and then returned.
     * 
     * @return The Entry for today.
     */
    public Entry getEntryOfTheDay();
    
    /**
     * Returns the diary's Entry for a given day.
     * <p>
     * Only one Entry per day can exists so, if the database already has the
     * Entry for day, the existing one will be returned. Otherwise a new
     * Entry will be created, inserted in the database and then returned.
     * 
     * @param day
     * @return The Entry for day.
     */
    public Entry getEntryOfTheDay(Calendar day);
    
    /** Returns the list of all the Entry saved in the database
     * <p>
     * @return the list of Entry (can be empty if the database has no Entry)
     */
    public List<Entry> getEntries();
    
    /** Insert a new text Note to the given Entry.
     * <p>
     * It's possible to add a Note to an Entry only during the day of creation
     * of the note.
     * 
     * @param entry
     * @param note_text The text of the Note
     * @throws InvalidOperationException When adding a Note to an Entry that
     *         is wasn't created today
     */
    public void insertNote(Entry entry, String note_text);
    
    /**
     * Updates the copy saved in the database of the given Note.
     * <p>
     * If you are trying to change the text of a Note saved in the database
     * and obtained via the {@link #getNotes(Entry)} method you should:
     *     1) Update the Note text using {@link NoteInterface#setText(String)}
     *     2) Update the copy of Note in the database with this method.
     * 
     * Each Note has a "grace period" during which it can be updated: calling
     * this method on a Note after the end of the grace period will result
     * in an error.
     *
     * @param note the updated version of the Note
     * @throws InvalidOperationException When note's grace period is already
     *         ended.
     */
    public void updateNote(Note note) throws InvalidOperationException;
    
    /**
     * Deletes the given Note from the database
     * 
     * @param note the Note to be deleted
     * @throws InvalidOperationException If the given Note isn't in the database
     */
    public void deleteNote(Note note) throws InvalidOperationException;
    
    /**
     * Returns the list of all the Note of the given Entry
     * 
     * @param entry
     * @return a List with all the Note of the Entry, can be empty.
     */
    public List<Note> getNotes(Entry entry);
    
    /**
     * Sets the mood for the given Entry
     * <p>
     * The Mood of an Entry can be modified only during the day the Entry was
     * created.
     * 
     * @param entry the Entry to update
     * @param mood the new Mood
     * @throws InvalidOperationException If the Entry's mood can't be modified
     */
    public void setMood(Entry entry, Mood mood) throws InvalidOperationException;
    
    /**
     * Returns the Mood of the given Entry
     * 
     * @param entry
     * @return the mood if the Entry has a Mood, otherwise null
     */
    public Mood getMood(Entry entry);
    
    /**
     * Sets the Mood for given Entry to null
     * 
     * @param entry
     */
    public void removeMood(Entry entry);
    
    /**
     * Sets the Photo for the given Entry
     * 
     * @param entry the Entry to be updated
     * @param photo the File object containing the photo
     * @return the newly created Photo object
     * 
     * @throws InvalidOperationException
     */
    public Photo setPhoto(Entry entry, File photo) throws InvalidOperationException;

    /**
     * Returns the Photo of the given Entry
     * 
     * @param entry the Entry 
     * @return the Photo object if the entry has a Photo, otherwise null
     */
    public Photo getPhoto(Entry entry);
    
    /**
     * Deletes the given photo from the database
     * 
     * A Photo can be deleted only during the same day it was took.
     * 
     * @param photo the Photo to be deleted
     * @throws InvalidOperationException When deleting a Photo that can't be
     *         deleted.
     */
    public void deletePhoto(Photo photo) throws InvalidOperationException;
    
    /**
     * Returns the list of all the Photo saved in the database.
     * 
     * @return the List with all the Photo
     */
    public List<Photo> getPhotos();
    
    /**
     * Return the list of all the Photo taken in the given time range
     * 
     * @param from the starting time of the range
     * @param to the ending time of the range
     * @return the List (can be empty) of matching Photo
     */
    public List<Photo> getPhotos(Calendar from, Calendar to);
    
}
