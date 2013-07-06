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
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Mood;

public class MoodActivity extends Activity {

	private DB database;
	private Entry entry;
    private Day day;
    private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        database = new DB(getApplicationContext());
        database.open();

        // Get the parameters passed with the Intent
        Intent intent = getIntent();
        long dayId = intent.getLongExtra(
                WriteEntry.EXTRA_MOOD_ACTIVITY_DayId,
                -1L
        );
        long entryId = intent.getLongExtra(
                WriteEntry.EXTRA_MOOD_ACTIVITY_EntryId,
                -1L
        );
        final String entryText = intent.getStringExtra(
                WriteEntry.EXTRA_MOOD_ACTIVITY_EntryText
        );

        day = database.getDay(dayId);
        entry = database.getEntry(entryId);

		GridView grid = (GridView)findViewById(R.id.moodGrid);
		final MoodAdapter imgAdapter = new MoodAdapter(this);
		grid.setAdapter(imgAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Mood mood = (Mood)imgAdapter.getItem(position);
	        	if(entry != null) {
                    // Updating an existing entry
	        		database.setEntryNote(entry, entryText);
	        		database.setEntryMood(entry, mood);
	        	}
	        	else{
                    // Creating a new one
	        		database.insertEntry(day, entryText, mood);
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
        // If:
        //  - updating an expired Entry
        // or:
        //  - adding a new Entry to an expired Day
        // return to the MainActivity
        if (entry != null && !entry.canBeUpdated(sharedPreferences) ||
                entry == null && day != null && !day.canBeUpdated()) {
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
