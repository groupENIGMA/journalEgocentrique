package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class WriteEntry extends Activity {

    public static final String EXTRA_MOOD_ACTIVITY_DayId = "DayId";
    public static final String EXTRA_MOOD_ACTIVITY_EntryId = "EntryId";
    public static final String EXTRA_MOOD_ACTIVITY_EntryText = "EntryText";

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
        selectedDay = dataBase.getDay(
                intent.getLong(MainActivity.EXTRA_WRITE_ENTRY_DayId)
        );
        long noteId = intent.getLong(MainActivity.EXTRA_WRITE_ENTRY_NoteId);
        if ( noteId == -1L) {
            // Creating a new note
            updating = false;
        }
        else {
            // Updating an existing Entry
            updating = true;
            selectedEntry = dataBase.getEntry(noteId);
            EditText text = (EditText) findViewById(R.id.editNote);
            text.append(selectedEntry.getNote());
        }
        // Open the shared preferences file
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, MoodActivity.class);
        intent.putExtra(EXTRA_MOOD_ACTIVITY_DayId, selectedDay.getId());
        intent.putExtra(EXTRA_MOOD_ACTIVITY_EntryText, message);
        if(updating) {
            intent.putExtra(EXTRA_MOOD_ACTIVITY_EntryId, selectedEntry.getId());
        }
        else {
            intent.putExtra(EXTRA_MOOD_ACTIVITY_EntryId, -1L);
        }
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
