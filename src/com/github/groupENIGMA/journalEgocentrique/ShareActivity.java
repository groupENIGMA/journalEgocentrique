package com.github.groupENIGMA.journalEgocentrique;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class ShareActivity extends Activity {

	private Day day;
	private DB db;
	private Entry note;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		db = new DB(getApplicationContext());
		db.open();
		
		Intent received = getIntent();
		day = db.getEntry(received.getLongExtra("EntryId", 0));
		note = null;
		
		displayNotes();
		createCustomPhoto();
	}
	
	// Create the union of mood and the photo
	private void createCustomPhoto(){
		// Per ora mette la foto del giorno
		ImageView img = (ImageView)findViewById(R.id.photoComposite);
		if(day.getPhoto() != null)
			img.setImageURI(Uri.parse(day.getPhoto().getPath()));
	 	Display display = getWindowManager().getDefaultDisplay();
    	int width = display.getWidth();
    	int height = display.getHeight();
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)img.getLayoutParams();
    	params.height = height/2;
    	params.width = width/2;
    	img.setLayoutParams(params);
	}
	
	// Creates the list of the notes. Only the selected will be sended.
	private void displayNotes(){
	 	Display display = getWindowManager().getDefaultDisplay();
    	int width = display.getWidth();
		List<Entry> notes = day.getEntries();
        ArrayAdapter<Entry> arrayAdapter = new ArrayAdapter<Entry>(
                this, R.layout.row, R.id.textViewList, notes
        );
        ListView list = (ListView)findViewById(R.id.notes);
        list.setAdapter(arrayAdapter);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)list.getLayoutParams();
        params.width = width /2;
        list.setLayoutParams(params);

        // Add the onLongClickListener that permits to choose the
        // note that will be sent
        OnItemClickListener clickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                int position, long id) {
                // Set the correct note that will be sent
                note = (Entry) adapter.getItemAtPosition(position);
            }
        };
        list.setOnItemClickListener(clickListener);
    }

	// Start the intent for sharing the composite photo and the selected note.
	// This method is invocated when the user press on SHARE! button
	public void share(View view){
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("*/*");
		if(note != null)
			share.putExtra(Intent.EXTRA_TEXT, note.getNote());
		share.putExtra(Intent.EXTRA_SUBJECT, "Created by ENIGMA");
		File tmp = new File(day.getPhoto().getPath());
		if(tmp != null)
			share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tmp));// per ora ho messo la photo poi vediamo di cambiare con la custom photo
		startActivity(Intent.createChooser(share, "Share to..."));
	}
}
