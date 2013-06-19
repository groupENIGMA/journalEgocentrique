package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Entry implements EntryInterface {

    private long id;
    private Calendar date;
    private Photo photo;
    private Mood mood;
    private ArrayList<Note> notes;

    /**
     * Creates an Entry
     * <p>
     * This constructor should be used only by a DBInterface implementation
     * 
     * @param id The unique id used by SQlite to identify the Entry
     * @param date The date of Entry
     * @param photo The photo for the Entry (can be null if the Entry doesn't
     * have one)
     * @param mood The mood of the Entry (can be null)
     * @param notes The list containing all the Notes of the Entry (can be
     * an empty List if the Entry doesn't have a Note)
     */
    protected Entry(long id, Calendar date, Photo photo, Mood mood,
            List<Note> notes) {
        this.id = id;
        this.date = date;
        this.photo = photo;
        this.mood = mood;
        this.notes = (ArrayList<Note>) notes;
    }

    @Override
    public Calendar getDay() {
        return this.date;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public Mood getMood() {
        return this.mood;
    }

    @Override
    public Photo getPhoto() {
        return this.photo;
    }

    @Override
    public List<Note> getNotes() {
        return this.notes;
    }

    @Override
    public boolean canBeDeleted() {
        return canBeUpdated();
    }

    @Override
    public boolean canBeUpdated() {
        Calendar today = Calendar.getInstance();
        // An Entry can be modified only during the day it was created
        if (today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                today.get(Calendar.DATE) == date.get(Calendar.DATE)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        if (id != entry.id) return false;
        if (!date.equals(entry.date)) return false;
        if (mood != null ? !mood.equals(entry.mood) : entry.mood != null)
            return false;
        if (!notes.equals(entry.notes)) return false;
        if (photo != null ? !photo.equals(entry.photo) : entry.photo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + date.hashCode();
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (mood != null ? mood.hashCode() : 0);
        result = 31 * result + notes.hashCode();
        return result;
    }
}
