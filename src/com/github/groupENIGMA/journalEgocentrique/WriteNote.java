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
	
	public final static String EXTRA_MESSAGE = "com.github.groupENIGMA.journalEgocentrique.MESSAGE";
	private boolean updating;
	private DB dataBase;
	private Entry selectedEntry;
	private Note selectedNote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_note);
		dataBase = new DB(getApplicationContext());
		dataBase.open();
		Bundle intent = getIntent().getExtras();
		if(intent != null){
			String oldMsg = intent.getString("OldMsg");
			updating = intent.getBoolean("Update");
			selectedEntry = dataBase.getEntry(intent.getLong("EntryId"));
			if(updating){
				EditText txt = (EditText)findViewById(R.id.editNote);
				txt.append(oldMsg);
				selectedNote = dataBase.getNote(intent.getLong("NoteId"));
			}
		}
	}
	
	public void sendNote(View view){
		Intent intent = new Intent(getApplicationContext(), ListActivity.class);
		EditText text = (EditText) findViewById(R.id.editNote);
		String message = text.getText().toString();
		if(updating){
			// come faccio ad usare updateNote ??
			dataBase.updateNote(selectedNote, message);
		}
		else{
			dataBase.insertNote(selectedEntry, message);
		}
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	public void deleteNote(View view){
		Intent intent = new Intent(getApplicationContext(), ListActivity.class);
		if(selectedNote != null)
			dataBase.deleteNote(selectedNote);
		startActivity(intent);
		
	}

}
