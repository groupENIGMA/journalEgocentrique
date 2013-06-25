package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Day implements DayInterface {

    private long id;
    private Calendar date;
    private Photo photo;
    private Mood mood;
    private ArrayList<Entry> entries;

    /**
     * Creates an Day
     * <p>
     * This constructor should be used only by a {@link DBInterface}
     * implementation
     * 
     * @param id The unique id used by SQlite to identify the Day
     * @param date The date of Day
     * @param photo The photo for the Day (can be null if the Day doesn't
     *        have one)
     * @param mood The mood of the Day (can be null)
     * @param entries The list containing all the Entries of the Day (can be
     *        an empty List if the Day doesn't have an Entry)
     */
    protected Day(long id, Calendar date, Photo photo, Mood mood,
                  List<Entry> entries) {
        this.id = id;
        this.date = date;
        this.photo = photo;
        this.mood = mood;
        this.entries = (ArrayList<Entry>) entries;
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
    public Photo getPhoto() {
        return this.photo;
    }

    @Override
    public List<Entry> getEntries() {
        return this.entries;
    }

    @Override
    public boolean canBeUpdated() {
        Calendar today = Calendar.getInstance();
        // An Day can be modified only during the day it was created
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

        Day day = (Day) o;

        if (id != day.id) return false;
        if (!date.equals(day.date)) return false;
        if (mood != null ? !mood.equals(day.mood) : day.mood != null)
            return false;
        if (!entries.equals(day.entries)) return false;
        if (photo != null ? !photo.equals(day.photo) : day.photo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + date.hashCode();
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (mood != null ? mood.hashCode() : 0);
        result = 31 * result + entries.hashCode();
        return result;
    }
}
