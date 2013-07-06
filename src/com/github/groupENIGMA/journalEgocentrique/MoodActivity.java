package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Mood;

public class MoodActivity extends Activity {

	private DB database;
	private Entry myEntry;
    private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood);
		Intent intent = getIntent();
		long entryId = intent.getLongExtra("EntryId", -1L);
		final String entryText = intent.getStringExtra("EntryText");
		final boolean updating = intent.getBooleanExtra("Updating", false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		database = new DB(getApplicationContext());
		database.open();
		myEntry = database.getEntry(entryId);
		GridView grid = (GridView)findViewById(R.id.moodGrid);
		final MoodAdapter imgAdapter = new MoodAdapter(this);
		grid.setAdapter(imgAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Mood mood = (Mood)imgAdapter.getItem(position);
	        	if(updating){
	        		database.setEntryNote(myEntry, entryText);
	        		database.setEntryMood(myEntry, mood);
	        	}
	        	else{
	        		database.insertEntry(database.getDay(), entryText, mood);
	        	}
	            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	            startActivity(intent);
	            Toast.makeText(getApplicationContext(), "Entry saved", Toast.LENGTH_SHORT).show();
	        }
	    });
	}

    @Override
    protected void onResume() {
        super.onResume();
        // Re-open database connection if it was closed by onPause()
        if (!database.isOpen()) {
            database.open();
        }
        // If the Entry "expired" when the Activity was suspended return to the
        // main Activity
        if (myEntry != null && !myEntry.canBeUpdated(sharedPreferences)) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }

	/**
	 * Close the database connection
	 */
	protected void onPause(){
		super.onPause();
		database.close();
	}

}
