package com.github.groupENIGMA.journalEgocentrique;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.*;

import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class MainActivity extends Activity {

    public final static String EXTRA_WRITENOTE_NoteId = "NoteId";
    public final static String EXTRA_WRITENOTE_DayId = "EntryId";
    public final static String EXTRA_MESSAGE = "com.github.groupENIGMA.journalEgocentrique.MESSAGE";

    private final static String PREF_SELECTED_ENTRY = "selectedEntry_id";

    private DB dataBase;
    private DaysArrayAdapter daysListArrayAdapter;
    private Day selectedDay = null;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        dataBase = new DB(getApplicationContext());

        // Open the shared preferences file
        sharedPreferences = getSharedPreferences(
                AppConstants.SHARED_PREFERENCES_FILENAME,
                MODE_PRIVATE
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open database connection
        dataBase.open();
        // Display the list of days with an Entry
        displayDaysList();


        // Display the last viewed Day (if any)
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        long id = pref.getLong(PREF_SELECTED_ENTRY, -1L);
        if(id != -1) {
            selectedDay = dataBase.getDay(id);
            // Display the Photo and Mood Image
            displayImages();
            // Display the Notes
            ListView notesListView = (ListView)findViewById(R.id.notes);
            displayNotes(notesListView);
        }
        else {
            selectedDay = null;
        }

        // If the Entry for today already exists disable the AddEntry button
        if (dataBase.existsDay()) {
            Button addEntry = (Button)findViewById(R.id.ListDaysAddEntryButton);
            addEntry.setEnabled(false);
        }
    }

    /**
     * Display the list of all Dates having an associated Day
     * It is also created a OnItemClickListener that at the click will display
     * the details of the day.
     */
    private void displayDaysList(){
        // Get the ListView that display the days
        ListView daysListView = (ListView)findViewById(R.id.daysList);

        // Get the list of available days from the database
        List<Calendar> daysList = dataBase.getDatesList();

        // Create and set the custom ArrayAdapter DaysArrayAdapter
        daysListArrayAdapter = new DaysArrayAdapter(
                this, R.layout.row, daysList
        );
        daysListView.setAdapter(daysListArrayAdapter);

        // Set the listener
        OnItemClickListener clickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {
                selectedDay = dataBase.getDay(
                        (Calendar) adapter.getItemAtPosition(position)
                );
                // Refresh notes and images
                displayImages();
                // Display the Notes
                ListView notesListView = (ListView)findViewById(R.id.notes);
                displayNotes(notesListView);
                // Adds the header with the photos
                View headerView = ((LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.main_detail_photos, null, false);
                notesListView.addHeaderView(headerView);
            }
        };
        daysListView.setOnItemClickListener(clickListener);
    }

    /**
     * Displays the Notes of the selectedDay
     *
     * @param list The ListView that will be used to display the Day
     */
    private void displayNotes(ListView list) {
        // Display the Notes
        List<Entry> notes = selectedDay.getEntries();
/*        ArrayAdapter<Entry> arrayAdapter = new ArrayAdapter<Entry>(
                this, R.layout.row, R.id.textViewList, notes
        );
        list.setAdapter(arrayAdapter);*/
        EntryAdapter entryAdapter = new EntryAdapter(this, R.layout.row_image, notes);
        list.setAdapter(entryAdapter);

        // Add the onLongClickListener that activates the WriteNote activity
        // that can be used to update the Entry text
        OnItemLongClickListener clickListener = new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view,
                int position, long id) {
                // Enable the onLongClickListener only if the Entry can be
                // updated.
                Entry selectedNote = (Entry) adapter.getItemAtPosition(position);
                if (selectedNote.canBeUpdated(sharedPreferences)) {
                    Intent intent = new Intent(
                            getApplicationContext(), WriteNote.class
                    );
                    intent.putExtra(
                            EXTRA_WRITENOTE_NoteId, selectedNote.getId()
                    );
                    startActivity(intent);
                    return true;
                }
                // The Entry can't be updated
                else {
                    return false;
                }
            }
        };
        list.setOnItemLongClickListener(clickListener);
    }

    /**
     * Sets the correct image for photo and mood selected by the user.
     */
    private void displayImages(){
        /*
        * No selected entry; display the default images
        */
        if(selectedDay == null){
            ImageView img = (ImageView) findViewById(R.id.dailyPhoto);
            img.setImageResource(R.drawable.ic_launcher);
        }
        /*
         * Day selected: display its images (if any) or the default ones
         * If the Day is editable also add the listeners that activate
         * PhotoActivity to change the Photo
         */
        else{
            boolean editable = selectedDay.canBeUpdated();
            ImageView img = (ImageView) findViewById(R.id.dailyPhoto);
            if(selectedDay.getPhoto() != null)
                img.setImageURI(Uri.parse(selectedDay.getPhoto().getPath()));
            else{
            	img.setImageResource(R.drawable.ic_launcher);
            //	dataBase.setPhoto(selectedDay, ((BitmapDrawable)img.getDrawable()).getBitmap());
            }
            if(editable){
                img.setOnTouchListener(new OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        // qui carica la vista per la fotoCamera
                        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);//ho messo PhotoActivity.class
                        intent.putExtra(EXTRA_MESSAGE, selectedDay.getId());
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
        // Get the preference file
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        // Save selected Day (if any)
        if (selectedDay == null) {
            edit.putLong(PREF_SELECTED_ENTRY, -1L);
        }
        else {
            edit.putLong(PREF_SELECTED_ENTRY, selectedDay.getId());
        }
        edit.commit();
        // Close database connection
        dataBase.close();
    }

    /**
     * Adds an Entry for today to the database and to the displayed list.
     * Used by ListDaysAddEntryButton in main.xml
     *
     * @param view as required by android:onClick xml attribute. Not used.
     */
    public void addTodayEntry(View view) {
        // New Entry in the database
        selectedDay = dataBase.createDay();
        // Entry to the beginning of the displayed list
        daysListArrayAdapter.insert(selectedDay.getDate(), 0);
        // Disable the ListDaysAddEntryButton
        Button addEntry = (Button)findViewById(R.id.ListDaysAddEntryButton);
        addEntry.setEnabled(false);
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
            case R.id.newNote:
                Intent intent = new Intent(
                        getApplicationContext(), WriteNote.class
                );
                intent.putExtra(EXTRA_WRITENOTE_NoteId, -1L);
                intent.putExtra(EXTRA_WRITENOTE_DayId, selectedDay.getId());
                startActivity(intent);
                return true;
	        case R.id.settings:
	        	Intent settings = new Intent(getApplicationContext(), Settings.class);
	        	startActivity(settings);
	        	return true;
	        case R.id.deleteEntry:
	        	if(selectedDay != null){
	        		dataBase.deleteDay(selectedDay);
	        		selectedDay = null;
	        		return true;
	        	}
	        case R.id.gallery:
	        	Intent gallery = new Intent(getApplicationContext(), GalleryActivity.class);
	        	startActivity(gallery);
	        	return true;
	        case R.id.share:
	        	Intent share = new Intent(getApplicationContext(), ShareActivity.class);
	        	share.putExtra("EntryId", selectedDay.getId());
	        	startActivity(share);
                return true;
	    }
		return false;
	}
}
