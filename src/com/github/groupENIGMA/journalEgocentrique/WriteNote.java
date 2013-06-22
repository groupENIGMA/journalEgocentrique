package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Note;

public class WriteNote extends Activity {

    private DB dataBase;
    private boolean updating;
    private Entry selectedEntry;
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        // Open the connection to the database
        dataBase = new DB(getApplicationContext());
        dataBase.open();
        // Check if we are updating or creating a Note
        Bundle intent = getIntent().getExtras();
        long noteId = intent.getLong(ListActivity.EXTRA_WRITENOTE_NoteId);
        if ( noteId == -1L) {
            // Creating a new note
            updating = false;
            selectedEntry = dataBase.getEntry(
                    intent.getLong(ListActivity.EXTRA_WRITENOTE_EntryId)
            );
        }
        else {
            // Updating an existing Note
            updating = true;
            selectedNote = dataBase.getNote(noteId);
            EditText text = (EditText) findViewById(R.id.editNote);
            text.setText(selectedNote.getText());
        }
    }

    public void saveNote(View view) {
        // Save the text
        EditText text = (EditText) findViewById(R.id.editNote);
        String message = text.getText().toString();
        if(updating){
            dataBase.updateNote(selectedNote, message);
        }
        else{
            dataBase.insertNote(selectedEntry, message);
        }
        // Return to ListActivity
        startActivity(new Intent(this, ListActivity.class));
    }

    public void deleteNote(View view){
        if(selectedNote != null) {
            dataBase.deleteNote(selectedNote);
        }
        startActivity(new Intent(this, ListActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reopen the database connection if it was closed
        if (!dataBase.isOpen()) {
            dataBase.open();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBase.close();
    }
}
