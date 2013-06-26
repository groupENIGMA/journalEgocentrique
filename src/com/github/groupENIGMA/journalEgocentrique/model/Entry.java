package com.github.groupENIGMA.journalEgocentrique.model;

import android.content.SharedPreferences;
import com.github.groupENIGMA.journalEgocentrique.AppConstants;

import java.util.Calendar;

public class Entry implements EntryInterface {

    private long id;
    private Calendar time;
    private String note;
    private Mood mood;

    /**
     * Create a new Entry with the given id, note and mood
     * <p>
     * This constructor should be used only by a {@link DBInterface}
     * implementation
     *
     * @param id the Entry id
     * @param time the Calendar with date and time of Entry creation
     * @param note the text note of the Entry
     * @param mood the Mood of the Entry
     */
    protected Entry(long id, Calendar time, String note, Mood mood) {
        this.id = id;
        this.time = time;
        this.note = note;
        this.mood = mood;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public Calendar getTime() {
        return this.time;
    }

    @Override
    public String getNote() {
        return this.note;
    }

    /**
     * Sets the text note of the Entry
     * <p>
     * Should be called only by {@link DBInterface} implementations.
     * Only updates the object field, not the Entry saved the database.
     * If you are looking for a method to update an Entry object and its copy in
     * the database use {@link DBInterface#setEntryNote(Entry, String)}
     *
     * @param note The new note
     */
    protected void setNote(String note) {
        this.note = note;
    }

    @Override
    public Mood getMood() {
        return this.mood;
    }

    /**
     * Sets the Mood of the Entry
     * <p>
     * Should be called only by {@link DBInterface} implementations.
     * Only updates the object field, not the Entry saved the database.
     * If you are looking for a method to update an Entry object and its copy in
     * the database use {@link DBInterface#setEntryMood(Entry, Mood)}
     *
     * @param mood The new mood. If you want to remove the mood you can use null
     */
    protected void setMood(Mood mood) {
        this.mood = mood;
    }

    @Override
    public boolean canBeUpdated(SharedPreferences preferences) {
        // Get the timeout from the shared preferences
        int hours_timeout = preferences.getInt(
                AppConstants.PREFERENCES_KEY_ENTRY_TIMEOUT,
                AppConstants.DEFAULT_NOTE_TIMEOUT
        );
        // Prepare a Calendar set to when the timeout for this Entry expires
        Calendar timeout = (Calendar) getTime().clone();
        timeout.add(Calendar.HOUR, hours_timeout);
        // Is the timeout expired?
        Calendar rightNow = Calendar.getInstance();
        if (rightNow.compareTo(timeout) >= 0) {
            return false;  // An expired Entry can't be updated
        }
        else {
            return true;
        }
    }

    @Override
    public String toString() {
        return getNote();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        if (id != entry.id) return false;
        if (mood != null ? !mood.equals(entry.mood) : entry.mood != null)
            return false;
        if (!note.equals(entry.note)) return false;
        if (!time.equals(entry.time)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + time.hashCode();
        result = 31 * result + note.hashCode();
        result = 31 * result + (mood != null ? mood.hashCode() : 0);
        return result;
    }
}
