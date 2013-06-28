package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class WriteNote extends Activity {

    private DB dataBase;
    private boolean updating;
    private Day selectedDay;
    private Entry selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        setView();
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
            selectedNote = dataBase.getEntry(noteId);
            EditText text = (EditText) findViewById(R.id.editNote);
            text.append(selectedNote.getNote());
        }
    }
    
    /**
     * Sets dinamically proportioned the size of the EditText
     */
    private void setView(){
    	Display display = getWindowManager().getDefaultDisplay();
    	int width = display.getWidth();
    	int height = display.getHeight();
    	EditText text = (EditText)findViewById(R.id.editNote);
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)text.getLayoutParams();
    	params.height = height;
    	params.width = width/2;
    	text.setLayoutParams(params);
    }

    public void saveNote(View view) {
        // Save the text
        EditText text = (EditText) findViewById(R.id.editNote);
        String message = text.getText().toString();
        if(updating){
            dataBase.setEntryNote(selectedNote, message);
        }
        else{
            dataBase.insertEntry(selectedDay, message, null);
        }
        // Return to MainActivity
        startActivity(new Intent(this, MainActivity.class));
    }

    public void deleteNote(View view){
        if(selectedNote != null) {
            dataBase.deleteEntry(selectedNote);
        }
        startActivity(new Intent(this, MainActivity.class));
    }
    
    public void setMood(View view){
        // Save the text
        EditText text = (EditText) findViewById(R.id.editNote);
        String message = text.getText().toString();
        if(updating){
            dataBase.setEntryNote(selectedNote, message);
        }
        else{
            selectedNote = dataBase.insertEntry(selectedDay, message, null);
        }
    	Intent intent = new Intent(this, MoodActivity.class);
    	Log.d("AAA", (selectedNote == null) + "");
    	intent.putExtra("EntryId", selectedNote.getId());
    	startActivity(intent);
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
