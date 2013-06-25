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

    @Override
    public Mood getMood() {
        return this.mood;
    }

    @Override
    public boolean canBeUpdated(SharedPreferences preferences) {
        // Get the timeout from the shared preferences
        int hours_timeout = preferences.getInt(
                AppConstants.PREFERENCES_KEY_NOTE_TIMEOUT,
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

        Entry note = (Entry) o;

        if (id != note.id) return false;
        if (!this.note.equals(note.note)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + note.hashCode();
        return result;
    }
}
