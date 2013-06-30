package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class WriteEntry extends Activity {

    private DB dataBase;
    private boolean updating;
    private Day selectedDay;
    private Entry selectedEntry;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_entry);
        // Open the connection to the database
        dataBase = new DB(getApplicationContext());
        dataBase.open();
        // Check if we are updating or creating a Entry
        Bundle intent = getIntent().getExtras();
        long noteId = intent.getLong(MainActivity.EXTRA_WRITE_NOTE_NoteId);
        if ( noteId == -1L) {
            // Creating a new note
            updating = false;
            selectedDay = dataBase.getDay(
                    intent.getLong(MainActivity.EXTRA_WRITE_NOTE_DayId)
            );
        }
        else {
            // Updating an existing Entry
            updating = true;
            selectedEntry = dataBase.getEntry(noteId);
            EditText text = (EditText) findViewById(R.id.editNote);
            text.append(selectedEntry.getNote());
        }
        // Open the shared preferences file
        sharedPreferences = getSharedPreferences(
                AppConstants.SHARED_PREFERENCES_FILENAME,
                MODE_PRIVATE
        );
    }

    /**
     * Save the current note.
     * @param view
     */
    public void saveNote(View view) {
        // Save the text
        EditText text = (EditText) findViewById(R.id.editNote);
        String message = text.getText().toString();
        if(updating){
            dataBase.setEntryNote(selectedEntry, message);
        }
        else{
            dataBase.insertEntry(selectedDay, message, null);
        }
        // Return to MainActivity
        startActivity(new Intent(this, MainActivity.class));
    }
    
    /**
     * Sets a mood for the current note launching an intent
     * to MoodActivity.
     * @param view
     */
    public void setMood(View view){
        // Save the text
        EditText text = (EditText) findViewById(R.id.editNote);
        String message = text.getText().toString();
        if(updating){
            dataBase.setEntryNote(selectedEntry, message);
        }
        else{
            selectedEntry = dataBase.insertEntry(selectedDay, message, null);
        }
    	Intent intent = new Intent(this, MoodActivity.class);
    	intent.putExtra("EntryId", selectedEntry.getId());
    	startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reopen the database connection if it was closed
        if (!dataBase.isOpen()) {
            dataBase.open();
        }
        // If the selectedDay or selectedEntry "expired" when the Activity
        // was suspended return to the main Activity
        if ((selectedDay != null && !selectedDay.canBeUpdated()) ||
                (selectedEntry != null && updating &&
                        !selectedEntry.canBeUpdated(sharedPreferences))) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBase.close();
    }
}
