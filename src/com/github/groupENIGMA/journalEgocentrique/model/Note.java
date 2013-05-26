package com.github.groupENIGMA.journalEgocentrique.model;

public class Note implements NoteInterface {

    private long id;
    private String text;

    /**
     * Create a new Note with the given id and text
     *
     * @param id the Note id
     * @param text the text of the Note
     */
    protected Note(long id, String text) {
        this.id = id;
        this.text = text;
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
