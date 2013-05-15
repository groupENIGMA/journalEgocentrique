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
    
    /** Returns the list of days that have an Entry associated
     * <p>
     * @return the list of days in the diary, can be empty on a brand new
     *         database
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
     */
    public Note insertNote(Entry entry, String note_text);
    
    /**
     * Updates the text note of the given Note
     * <p>
     * Each Note has a "grace period" during which it can be updated: calling
     * this method on a Note after the end of the grace period will result
     * in an error.
     *
     * @param note, the Node you want to update
     * @param new_note_text, the new text of the note
     * @return the new version of the Note
     * @throws InvalidOperationException When note's grace period is already
     *         ended.
     */
    public Note updateNote(Note note, String new_note_text)
    		throws InvalidOperationException;
    
    /**
     * Deletes the given Note from the database
     * 
     * @param note the Note to be deleted
     * @throws InvalidOperationException If the given Note isn't in the database
     */
    public void deleteNote(Note note) throws InvalidOperationException;
    
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
     * Sets the Mood for given Entry to null
     * 
     * @param entry
     */
    public void removeMood(Entry entry);
    
    /** Returns the list of available Moods.
     * <p>
     * The user can choose to assign to the entries one of these Moods.
     * 
     * @return the list of available Moods
     */
    public List<Mood> getAvailableMoods();
    
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
