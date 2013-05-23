package com.github.groupENIGMA.journalEgocentrique.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.database.Cursor;

import java.text.SimpleDateFormat;
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
    public static final String NOTE_DATE = "NoteDate";

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

        this.open();

        String date_string = date_format.format(day.getTime());

        Cursor curE = db.rawQuery(
                "SELECT * " +
                " FROM " + Entry_TABLE +
                " WHERE " + DATE + "=?",
                new String[] {date_string}
        );
        Cursor curN = db.rawQuery(
                "SELECT *" +
                " FROM " + Notes_TABLE +
                " WHERE " + NOTE_DATE + "=?", 
                new String [] {date_string}
        );

        if(curE == null){
            db.close();
            return new Entry(day);
        }

        long id = curE.getLong(1);                  //First column is ID
        Photo ph = new Photo(curE.getString(3));    //Third column is Photo
        Mood mo = new Mood(curE.getInt(4));         //Fourth column is Mood
        curE.close();

        if(curN != null){
            // code that fills a list of notes and passes it to the Entry constructor
            // Note [] no      ... non array ma arrayList o List
        }
        db.close();
        return new Entry(id, day, ph, mo, no);


    }

    /**
     * {@inheritDoc}
     */
    public List<Calendar> getDays() {

    }

    /**
     * {@inheritDoc}
     */
    public Note insertNote(Entry entry, String note_text) throws InvalidOperationException {

    }

    /**
     * {@inheritDoc}
     */
    public Note updateNote(Note note, String new_note_text) throws InvalidOperationException {

    }

    /**
     * {@inheritDoc}
     */
    public void deleteNote(Note note) throws InvalidOperationException {

    }

    /**
     * {@inheritDoc}
     */
    public void setMood(Entry entry, Mood mood) throws InvalidOperationException {

    }

    /**
     * {@inheritDoc}
     */
    public void removeMood(Entry entry) throws InvalidOperationException {

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
                    ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    DATE + " TEXT UNIQUE NOT NULL," +
                    PHOTO + " TEXT," +
                    MOOD + " INTEGER " +
                    " );";
            String newNotesTable =
                    "CREATE TABLE IF NOT EXISTS " + Notes_TABLE + " ( " +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    NOTE_TEXT + " TEXT," +
                    
                    "CONSTRAINT fk_Notes FOREIGN KEY(" + NOTE_DATE + ") REFERENCES " +
                    Entry_TABLE + "(" + DATE + ") ON DELETE CASCADE" +
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

