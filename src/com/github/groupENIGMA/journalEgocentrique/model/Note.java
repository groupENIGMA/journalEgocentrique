package com.github.groupENIGMA.journalEgocentrique.model;

import java.util.Calendar;

public class Note implements NoteInterface {

    private long id;
    private String text;
    private Calendar time;

    /**
     * Create a new Note with the given id and text
     *
     * @param id the Note id
     * @param text the text of the Note
     * @param time the Calendar with date and time of Note creation
     */
    protected Note(long id, String text, Calendar time) {
        this.id = id;
        this.text = text;
        this.time = time;
    }

    @Override
    public String getText() {
        return this.text;
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
    public boolean canBeUpdated() {
        // TODO Auto-generated method stub
        return false;

    }

    @Override
    public boolean canBeDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (id != note.id) return false;
        if (!text.equals(note.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + text.hashCode();
        return result;
    }
}
