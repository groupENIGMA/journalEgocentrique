package com.github.groupENIGMA.journalEgocentrique;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Note;

public class ListActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.github.groupENIGMA.journalEgocentrique.MESSAGE";
    private List<Calendar> daysList;
    private DB dataBase;
    private Entry selectedEntry = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        dataBase = new DB(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open database connection
        dataBase.open();
        // Display the list of days with an Entry
        daysList = dataBase.getDays();
        ListView daysListView = (ListView)findViewById(R.id.list);
        displayDaysList(daysListView, daysList);

        // Display the last viewed Entry (if any)
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        long id = pref.getLong("Id", -1);
        if(id != -1) {
            selectedEntry = dataBase.getEntry(id);
            // Display the Photo and Mood Image
            displayImages();
            // Display the Notes
            ListView notesListView = (ListView)findViewById(R.id.notes);
            displayNotes(notesListView);
        }
        else {
            selectedEntry = null;
        }
    }

    /**
     * Display the list of all Days having an associated Entry
     * It is also created a OnItemClickListener that at the click will display
     * the details of the day.
     *
     * @param list The list to populate.
     * @param entry With this List we will populate the ListView
     */
    private void displayDaysList(ListView list, List<Calendar> entry){
        // Create and set the custom ArrayAdapter DaysArrayAdapter
        DaysArrayAdapter arrayAdapter = new DaysArrayAdapter(
                this, R.layout.row, entry
        );
        list.setAdapter(arrayAdapter);

        // Set the listener
        OnItemClickListener clickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {
                selectedEntry = dataBase.getEntry(
                        (Calendar)adapter.getItemAtPosition(position)
                );
            }
        };
        list.setOnItemClickListener(clickListener);
    }

    /**
     * Displays the Notes of the selectedEntry
     *
     * @param list The ListView that will be used to display the Entry
     */
    private void displayNotes(ListView list) {
        boolean editable = selectedEntry.canBeUpdated();
        final List<Note> tmp = selectedEntry.getNotes();
        List<String> notes = new ArrayList<String>();
        for(int i = 0;i < tmp.size();i++){
            notes.add(tmp.get(i).getText());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.textViewList, notes);
        list.setAdapter(arrayAdapter);
        if(editable){
	        OnItemLongClickListener clickListener = new OnItemLongClickListener() {
	
	            @Override
	            public boolean onItemLongClick(AdapterView<?> adapter, View view,
	                int position, long id) {
	                String modify = (String)adapter.getItemAtPosition(position);
	                Log.d("Invio", modify);
	                Intent intent = new Intent(getApplicationContext(), WriteNote.class);//ho messo WriteNote.class
	                intent.putExtra("OldMsg", modify.toString());
	                intent.putExtra("Update", true);
	                intent.putExtra("EntryId", selectedEntry.getId());
	                intent.putExtra("NoteId", tmp.get(position).getId());
	                Log.d("Note id", tmp.get(position).getId()+"");
	                startActivity(intent);
	                return true;
	            }
	        };
	        list.setOnItemLongClickListener(clickListener);
        }
    }

    /**
     * Sets the correct image for photo and mood selected by the user.
     */
    private void displayImages(){
        /*
        * No selected entry; display the default images
        */
        if(selectedEntry == null){
            ImageView img = (ImageView) findViewById(R.id.dailyPhoto);
            img.setImageResource(R.drawable.ic_launcher);
            img = (ImageView)findViewById(R.id.emoticon);
            img.setImageResource(R.drawable.ic_launcher);
        }
        /*
         * Entry selected: display its images (if any) or the default ones
         * If the Entry is editable also add the listeners that activate
         * MoodActivity and PhotoActivity.to change the Mood and Photo
         */
        else{
            boolean editable = selectedEntry.canBeUpdated();
            ImageView img = (ImageView) findViewById(R.id.dailyPhoto);
            if(selectedEntry.getPhoto() != null)
                img.setImageURI(Uri.parse(selectedEntry.getPhoto().getPath()));
            else
                img.setImageResource(R.drawable.ic_launcher);
            ImageView mood = (ImageView)findViewById(R.id.emoticon);
            if(selectedEntry.getMood() == null)
                mood.setImageResource(R.drawable.ic_launcher);
            else
                mood.setImageResource((selectedEntry.getMood().getEmoteId(getApplicationContext())));
            if(editable){
                img.setOnTouchListener(new OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        // qui carica la vista per la fotoCamera
                        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);//ho messo PhotoActivity.class
                        intent.putExtra(EXTRA_MESSAGE, selectedEntry.getId());
                        startActivity(intent);
                        return false;
                    }
                });
                mood.setOnTouchListener(new OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        // qui carica la vista per il moood
                        Intent intent = new Intent(getApplicationContext(), MoodActivity.class);//ho messo MoodActivity.class
                        intent.putExtra(EXTRA_MESSAGE, selectedEntry.getId());
                        startActivity(intent);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        // Save selected Entry
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong("Id", selectedEntry.getId());
        edit.commit();
        // Close database connection
        dataBase.close();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.newEntry:
	            selectedEntry = dataBase.createEntry();
	            Log.e("New Entry", selectedEntry.getId()+"");//debug
	            daysList = dataBase.getDays();
	    	    ListView list = (ListView)findViewById(R.id.list);
	    	    displayDaysList(list, daysList);
	            return true;
	        case R.id.newNote:
	            Intent intent = new Intent(getApplicationContext(), WriteNote.class);
	            intent.putExtra("Update", false);
	            intent.putExtra("EntryId", selectedEntry.getId());
	            startActivity(intent);
	            return true;
	        case R.id.settings:
	        	Intent settings = new Intent(getApplicationContext(), Settings.class);
	        	startActivity(settings);
	        	return true;
	        case R.id.deleteEntry:
	        	if(selectedEntry != null){
	        		//dataBase.deleteEntry(selectedEntry);
	        		return true;
	        	}
	        case R.id.gallery:
	        	Intent gallery = new Intent(getApplicationContext(), GalleryActivity.class);
	        	startActivity(gallery);
	    }
		return false;
	}
}
