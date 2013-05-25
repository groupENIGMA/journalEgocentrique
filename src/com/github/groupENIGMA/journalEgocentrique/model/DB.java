package com.github.groupENIGMA.journalEgocentrique.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.database.Cursor;
import android.content.ContentValues;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class implements the Application Database.
 * It creates or updates the database, if already existing
 * it also implements methods to manage the stored data for each Entry
 * 
 * @version 0.1 
 * @author groupENIGMA
 */
public class DB implements DBInterface {

    // Constants about the database
    private static final String DB_NAME = "JournalEgocentrique.db"; 
    private static final int DB_VERSION = 1; 

    // Constants specified for the Entry table and its fields
    public static final String Entry_TABLE = "Entry"; 
    public static final String ENTRY_ID = "_id";
    public static final String DATE = "Date";
    public static final String PHOTO = "Photo";
    public static final String MOOD = "Mood";

    // Constants specified for the Notes table and its fields
    public static final String Notes_TABLE = "Notes";
    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "NoteText";
    public static final String NOTE_ENTRYID = "EntryID";

    // Format used by the dates saved in the database
    public static final String DB_DATE_FORMAT= "yyyy-MM-dd";

    // A reference to the database used by the application
    private SQLiteDatabase db;

    // the Activity or Application that is creating an object from this class.
    Context context;
    openHelper helper;
    
    // Used to convert Calendar to String and String to Calendar
    SimpleDateFormat date_format;

    //The database manager constructor
    public DB(Context context) {

        this.context = context;
        // create or open the database
        helper = new openHelper(context);
        date_format = new SimpleDateFormat(DB_DATE_FORMAT);
    }

    /**
     * {@inheritDoc}
     */
    public void open() throws SQLException {

        db = helper.getWritableDatabase();
    }

    /**
     * {@inheritDoc}
     */
    public void close() {

        db.close();
    }

    /** Reads the rows available in the given Cursor to create and return
     * an Entry object with its related Notes
     * <p>
     * This works correctly only if the rows available in the cursor have the
     * following columns in this exact order:
     *      ENTRY_ID, DATE, PHOTO, MOOD, NOTE_ID, NOTE_TEXT
     * (it's basically the Entry table joined with the Note one)
     * 
     * @param cur The Cursor containing the rows with the Entry data
     * @returns an Entry or, if the cur is "empty", null
     */
    private Entry createEntryWithNotesFromCursor(Cursor cur) {
        // Check if the Cursor has at least one row
        if (cur.getCount() == 0) {
            return null;
        }
        else {
            // Move to the first row
            cur.moveToFirst();
            // Get the Entry id
            long entry_id = cur.getLong(0);
            // Get the Entry date
            Calendar entry_date = Calendar.getInstance();
            try {
                entry_date.setTime(date_format.parse(cur.getString(1)));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;  // This maybe should be changed with an exception
            }
            // Get the Entry Photo (if exists)
            Photo entry_photo;
            if (cur.isNull(2)) {
                entry_photo = null;
            }
            else {
                entry_photo= new Photo(cur.getString(2));
            }
            // Get the Entry Mood (if exists)
            Mood entry_mood;
            if (cur.isNull(3)) {
                entry_mood = null;
            }
            else {
                entry_mood = new Mood(cur.getLong(3));
            }
            // Get all the Notes
            ArrayList<Note> note_list = new ArrayList<Note>();
            if (!cur.isNull(4)) {  // Notes are available only if NOTE_ID!=NULL
                do {
                    Note note = new Note(
                            cur.getLong(4),     // NOTE_ID
                            cur.getString(5)    // NOTE_TEXT
                            );
                    note_list.add(note);
                }
                while (cur.moveToNext());
            }
            // Create and return the Entry
            return new Entry(entry_id, entry_date, entry_photo, entry_mood,
                    note_list);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Entry getEntryOfTheDay() {

        Calendar cal = Calendar.getInstance();
        return this.getEntryOfTheDay(cal);
    }

    /** 
     * {@inheritDoc}
     */
    public Entry getEntryOfTheDay(Calendar day) {
        // Convert the Calendar object to a String in the same format
        // used in the database
        String date_string = date_format.format(day.getTime());
        // Query the database
        Cursor cur = db.rawQuery(
                "SELECT " + Entry_TABLE + "." + ENTRY_ID    + ", " +
                            Entry_TABLE + "." + DATE        + ", " +
                            Entry_TABLE + "." + PHOTO       + ", " +
                            Entry_TABLE + "." + MOOD        + ", " +
                            Notes_TABLE + "." + NOTE_ID     + ", " +
                            Notes_TABLE + "." + NOTE_TEXT   +
                " FROM " + Entry_TABLE + " LEFT OUTER JOIN " + Notes_TABLE +
                            " ON " + Entry_TABLE + "." + ENTRY_ID + "=" +
                                     Notes_TABLE + "." + NOTE_ENTRYID +
                " WHERE " + DATE + "=?",
                new String[] {date_string}
        );

        Entry e = createEntryWithNotesFromCursor(cur);
        cur.close();
        return e;
    }

    /**
     * {@inheritDoc}
     */
    public Entry getEntry(long id) {
        // Query the database
        Cursor cur = db.rawQuery(
                "SELECT " + Entry_TABLE + "." + ENTRY_ID    + ", " +
                            Entry_TABLE + "." + DATE        + ", " +
                            Entry_TABLE + "." + PHOTO       + ", " +
                            Entry_TABLE + "." + MOOD        + ", " +
                            Notes_TABLE + "." + NOTE_ID     + ", " +
                            Notes_TABLE + "." + NOTE_TEXT   +
                " FROM " + Entry_TABLE + " LEFT OUTER JOIN " + Notes_TABLE +
                            " ON " + Entry_TABLE + "." + ENTRY_ID + "=" +
                                     Notes_TABLE + "." + NOTE_ENTRYID +
                " WHERE " + ENTRY_ID + "=?",
                new String[] {Long.toString(id)}
        );

        Entry e = createEntryWithNotesFromCursor(cur);
        cur.close();
        return e;
    }

    /**
     * {@inheritDoc}
     */
    public List<Calendar> getDays() {

    	//select all the days stored in the database (they are UNIQUE)
    	Cursor cur = db.rawQuery(
                "SELECT " + Entry_TABLE + "." + DATE,
                new String[] {}
                );
    	//processes the query result with the cursor
    	if (cur.getCount() == 0) {
    		//no entries in the Entry table
            return null;
        }
        else {
        	//Create the list to return
        	List<Calendar> days = new ArrayList<Calendar>();
        	//Create a calendar instance
        	Calendar date = Calendar.getInstance();
            // Move to the first row
            cur.moveToFirst();
            do{
                try {
                	//Fill the list with the dates
                    date.setTime(date_format.parse(cur.getString(1)));
                    days.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null; 
                }
            }
            while (cur.moveToNext());
            
            return days;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Note insertNote(Entry entry, String note_text) throws InvalidOperationException {
    	
    	long entryId = entry.getId();
    	ContentValues cv=new ContentValues();
    	
    	//Insert the note text referred to the entry id
    	cv.put(NOTE_TEXT, note_text);
    	cv.put(NOTE_ENTRYID, entryId);
    	long id = db.insert(Notes_TABLE, NOTE_ID, cv);                     
    	
    	Cursor cur = db.rawQuery(
                "SELECT " + Notes_TABLE + "." + NOTE_ID,          // DA VERIFICARE STA PARTE !! MANCA INSERIRE ID
                new String[] {}
                );
    	cur.moveToFirst();
    	
    	//Create the note to return
    	return new Note(id, note_text);
    }

    /**
     * {@inheritDoc}
     */
    public Note updateNote(Note note, String new_note_text) throws InvalidOperationException {
    	
    	ContentValues cv=new ContentValues();
    	
    	//Put the new text into the database at the chosen id
    	cv.put(NOTE_TEXT, new_note_text);
    	long id = db.update(Notes_TABLE, cv, NOTE_ID + "=?", new String []{String.valueOf(note.getId())});   
    	
    	return new Note(id, new_note_text);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteNote(Note note) throws InvalidOperationException {
    	
    	//Deletes the selected row from the Notes table in the database
    	db.delete(Notes_TABLE, NOTE_ID + "=?", new String [] {String.valueOf(note.getId())});
    }

    /**
     * {@inheritDoc}
     */
    public void setMood(Entry entry, Mood mood) throws InvalidOperationException {
    	
        ContentValues cv=new ContentValues();
    	
    	//Put the new path String in the mood column
    	cv.put(MOOD, mood.getPathImage());
    	db.update(Entry_TABLE, cv, ENTRY_ID + "=?", new String []{String.valueOf(entry.getId())});   
    }

    /**
     * {@inheritDoc}
     */
    public void removeMood(Entry entry) throws InvalidOperationException {

    	ContentValues cv=new ContentValues();
    	
    	//Removes the image path from the mood column
    	cv.putNull(MOOD);
    	db.update(Entry_TABLE, cv, ENTRY_ID + "=?", new String []{String.valueOf(entry.getId())});   
    }

    /**
     * {@inheritDoc}
     */
    public List<Mood> getAvailableMoods() {

    }

    /**
     * {@inheritDoc}
     */
    public Photo setPhoto(Entry entry, String path) throws InvalidOperationException {

        ContentValues cv=new ContentValues();
    	
    	//Put the new path String in the Photo column
    	cv.put(PHOTO, path);
    	db.update(Entry_TABLE, cv, ENTRY_ID + "=?", new String []{String.valueOf(entry.getId())});  
    	
    	return new Photo(path);
    }

    /**
     * {@inheritDoc}
     */
    public void deletePhoto(Photo photo) throws InvalidOperationException {

    }

    /**
     * {@inheritDoc}
     */
    public List<Photo> getPhotos() {

    }

    /**
     * {@inheritDoc}
     */
    public List<Photo> getPhotos(Calendar from, Calendar to) {

    }


    /**
     * This class implements an open helper extending the SQLiteOpenHelper
     * 
     * It creates, if not existing, a new database or updates the existing one
     */
    private class openHelper extends SQLiteOpenHelper {

        public openHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // SQL statements used to create the tables
            String newEntryTable = 
                    "CREATE TABLE IF NOT EXISTS" + Entry_TABLE + " ( " +
                    ENTRY_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    DATE        + " TEXT UNIQUE NOT NULL," +
                    PHOTO       + " TEXT," +
                    MOOD        + " INTEGER " +
                    " );";
            String newNotesTable =
                    "CREATE TABLE IF NOT EXISTS " + Notes_TABLE + " ( " +
                    NOTE_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    NOTE_TEXT       + " TEXT," +
                    NOTE_ENTRYID    + " INTEGER," +
                    "CONSTRAINT fk_Notes FOREIGN KEY(" + NOTE_ENTRYID + ") " +
                    "REFERENCES " + Entry_TABLE + "(" + ENTRY_ID + ")" +
                    " );";
            // Run all the CREATE TABLE ...
            db.execSQL(newEntryTable);
            db.execSQL(newNotesTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //
        }

        @Override
        public void onOpen(SQLiteDatabase db) {

            super.onOpen(db);
            if (!db.isReadOnly()) {
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
    }

}

