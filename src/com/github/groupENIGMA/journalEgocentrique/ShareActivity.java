package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class ShareActivity extends Activity {

	private Entry entry;
	private DB db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		db = new DB(getApplicationContext());
		db.open();
		
		Intent received = getIntent();
		entry = db.getEntry(received.getLongExtra("EntryId", 0));
		
	}
	
	// Create the union of mood and the photo
	private void createCustomPhoto(){
		
	}
	
	// Creates the list of the notes. Only the selected will be sended.
	private void setNotes(){
		// Use ArrayAdapter
	}

	// Start the intent for sharing the composite photo and the selected note.
	public void share(){
		
	}
}
