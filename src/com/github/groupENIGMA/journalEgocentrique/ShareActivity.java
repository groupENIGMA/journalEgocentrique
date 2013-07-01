package com.github.groupENIGMA.journalEgocentrique;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Photo;

public class ShareActivity extends Activity {

	private Day day;
	private DB db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		db = new DB(getApplicationContext());
		db.open();
		
		Intent received = getIntent();
		day = db.getDay(received.getLongExtra("EntryId", -1L));

		displayNotes();
		displayPhoto();
	}
	
	/**
	 * Display the day-photo that can be sent
	 */
	private void displayPhoto(){
		ImageView img = (ImageView)findViewById(R.id.sharePhotoView);
		if(day.getPhoto() != null)
			img.setImageURI(Uri.parse(day.getPhoto().getPath()));
	}
	
	/** 
	 * Creates the list of the notes. Only the selected will be sent.
	 */
	private void displayNotes(){
		List<Entry> entries = day.getEntries();
        ArrayAdapter<Entry> arrayAdapter = new ArrayAdapter<Entry>(
                this, R.layout.activity_share_note_row, entries
        );
        ListView list = (ListView)findViewById(R.id.shareNotes);
        list.setAdapter(arrayAdapter);

        /*
         *  Add the onLongClickListener that permits to choose the
         *  note that will be sent
        */
        OnItemClickListener clickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                int position, long id) {
                // Send a Share intent with data from teh selected Entry
                Entry entry = (Entry) adapter.getItemAtPosition(position);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("*/*");
                share.putExtra(Intent.EXTRA_TEXT, entry.getNote());
                share.putExtra(Intent.EXTRA_SUBJECT, "Created by ENIGMA");
                // Include the photo if available
                Photo dailyPhoto = day.getPhoto();
                if (dailyPhoto != null) {
                    File tmp = new File(dailyPhoto.getPath());
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tmp));
                }
                startActivity(Intent.createChooser(share, "Share to..."));
            }
        };
        list.setOnItemClickListener(clickListener);
    }
}
