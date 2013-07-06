package com.github.groupENIGMA.journalEgocentrique;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Photo;

public class MainActivity extends Activity {

    public final static String EXTRA_WRITE_NOTE_NoteId = "NoteId";
    public final static String EXTRA_WRITE_NOTE_DayId = "EntryId";
    public final static String EXTRA_PHOTO_ACTIVITY_DayId = "DayId";

    private final static String PREF_SELECTED_ENTRY = "selectedEntry_id";

    private DB dataBase;
    private DaysArrayAdapter daysListArrayAdapter;
    private Day selectedDay = null;
    private SharedPreferences sharedPreferences;
    private int fontSize = R.array.textSize;
    private Typeface fontTypeFace;

    // Views for the Detail Section of the UI
    ListView entryListView;
    ImageView dailyPhotoHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        dataBase = new DB(getApplicationContext());

        // Open the shared preferences file
        PreferenceManager.setDefaultValues(
                this, R.xml.preferences, false
        );
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        // Open database connection
        dataBase.open();
        // Display the list of days with an Entry
        displayMasterLayout();
        // Prepare the Detail Layout
        prepareDetailLayout();

        // Get the preferences values for fontSize, fontTypeFace
        // The cast is needed because PreferenceActivity saves only values as
        // strings
        String textSizeAsString = sharedPreferences.getString(
                getString(R.string.PreferencesKeyFontSize),
                null
        );
        fontSize = Integer.parseInt(textSizeAsString);
        fontTypeFace = Typeface.create(
                sharedPreferences.getString(
                        getString(R.string.PreferencesKeyFontTypeFace),
                        null
                ),
                Typeface.ITALIC
        );

        // Display the last viewed Day (if any) and the text size and font
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        long id = pref.getLong(PREF_SELECTED_ENTRY, -1L);
        if(id != -1L) {
            selectedDay = dataBase.getDay(id);
            displayDetailLayout();
        }
        else {
            selectedDay = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // Database connection must be reopened if the app was previously
        // "paused" with onPause()
        if (!dataBase.isOpen()) {
            dataBase.open();
        }
    }

    /**
     * Display the "Master" section of the UI (the list of Days)
     */
    private void displayMasterLayout() {
        // Get the ListView that display the days
        ListView daysListView = (ListView)findViewById(R.id.daysList);

        // Get the list of available days from the database
        List<Calendar> daysList = dataBase.getDatesList();

        // Create and set the custom ArrayAdapter DaysArrayAdapter
        daysListArrayAdapter = new DaysArrayAdapter(
                this, R.layout.main_row_day, daysList
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
                // Display the Detail of the selected day
                displayDetailLayout();
            }
        };
        daysListView.setOnItemClickListener(clickListener);
    }

    /**
     * Prepare the "Detail" section of the UI (Daily photo + Entry)
     *
     * This is in a separate method from displayDetailLayout because
     * addHeaderView can be called only once per ListView
     */
    private void prepareDetailLayout() {
        // Get the ListView
        entryListView = (ListView) findViewById(R.id.EntryList);
        // Get the View with the daily Photo
        LayoutInflater inflater = LayoutInflater.from(this);
        dailyPhotoHeader = (ImageView) inflater
                .inflate(R.layout.main_detail_photo_header, null, false);
        // Add the header with the DailyPhoto to the detailView
        entryListView.addHeaderView(dailyPhotoHeader);
    }

    /**
     * Display the "Detail" section of the UI (Daily photo + Entry)
     */
    private void displayDetailLayout() {
        // The Detail section will be displayed only when there's a Day selected
        if (selectedDay != null) {
            // Get the list of Entry for the selectedDay
            List<Entry> entries = selectedDay.getEntries();

            // Prepare the custom ArrayAdapter
            EntryAdapter entryAdapter = new EntryAdapter(
                    this, R.layout.main_row_entry, entries, fontSize, fontTypeFace
            );

            // Display the selected Day in the header
            SimpleDateFormat sdf = new SimpleDateFormat(
                    AppConstants.DISPLAY_DATE_FORMAT
            );
            TextView detailHeaderTitle = (TextView) findViewById(
                    R.id.MainDetailTitle
            );
            detailHeaderTitle.setText(
                    sdf.format(selectedDay.getDate().getTime())
            );

            // If available, display the Photo in the header
            Photo dailyPhoto = selectedDay.getPhoto();
            if (dailyPhoto != null) {
                String photoPath = dailyPhoto.getPath();
                dailyPhotoHeader.setImageURI(Uri.parse(photoPath));
            }
            // Display the default image
            else {
                dailyPhotoHeader.setImageResource(R.drawable.default_day_image);
            }

            // If the selected Day can be updated add the listener that starts
            // the PhotoActivity (to take a new Photo)
            if (selectedDay.canBeUpdated()) {
                dailyPhotoHeader.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start the PhotoActivity
                        Intent intent = new Intent(
                                getApplicationContext(),
                                PhotoActivity.class
                        );
                        intent.putExtra(
                                EXTRA_PHOTO_ACTIVITY_DayId,
                                selectedDay.getId()
                        );
                        startActivity(intent);
                    }
                });
            }
            // The Photo can't be updated
            else {
                dailyPhotoHeader.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        Toast.makeText(
                                getApplicationContext(),
                                "The photo can't be updated",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }

            // Set the custom ArrayAdapter to the detailView
            entryListView.setAdapter(entryAdapter);

            // Add the clickListener that activates the WriteEntry activity
            // that can be used to update the Entry text
            OnItemClickListener clickListener = new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter, View view,
                                               int position, long id) {
                    // Enable the onClickListener only if the Entry can be
                    // updated.
                    final Entry selectedEntry = (Entry) adapter
                            .getItemAtPosition(position);
                    // Create the Alert Dialog
                    AlertDialog.Builder build = new AlertDialog.Builder(
                            MainActivity.this
                    );
                    build.setMessage("Select the action");
                    // Add the share button
                    build.setNeutralButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, selectedEntry.getNote());
                            share.putExtra(Intent.EXTRA_SUBJECT, "Created by ENIGMA");
                            startActivity(Intent.createChooser(share, "Share to..."));
                        }
                    });
                    if (selectedEntry.canBeUpdated(sharedPreferences)) {

                        build.setNegativeButton("Delete", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dataBase.deleteEntry(
                                        selectedEntry,
                                        sharedPreferences
                                );
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Deleted",
                                        Toast.LENGTH_SHORT
                                ).show();
                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        MainActivity.class
                                );
                                startActivity(intent);
                            }
                        });

                        build.setPositiveButton("Update", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int id){
                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        WriteEntry.class
                                );
                                intent.putExtra(
                                        EXTRA_WRITE_NOTE_DayId,
                                        selectedDay.getId()
                                );
                                intent.putExtra(
                                        EXTRA_WRITE_NOTE_NoteId,
                                        selectedEntry.getId()
                                );
                                startActivity(intent);
                            }
                        });
                    }
                    AlertDialog alert = build.create();
                    alert.show();
                }
            };
            entryListView.setOnItemClickListener(clickListener);
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

    @Override
    public void onBackPressed(){
    	super.onBackPressed();
    	moveTaskToBack(true);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option, menu);
		return true;
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Enable or disable the "Add Entry" and "Delete Day" items in the
        // option menu based on the selectedDay
        MenuItem addEntry = menu.findItem(R.id.newEntry);
        MenuItem deleteDay = menu.findItem(R.id.deleteDay);
        if (selectedDay != null && selectedDay.canBeUpdated()) {
            addEntry.setEnabled(true);
            deleteDay.setEnabled(true);
        }
        else {
            addEntry.setEnabled(false);
            deleteDay.setEnabled(false);
        }
        // Enable or disable the "Add Day",
        MenuItem addDay = menu.findItem(R.id.newDay);
        if (dataBase.existsDay()) {
            addDay.setEnabled(false);
        }
        else {
            addDay.setEnabled(true);
        }
        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
            case R.id.newDay:
                // New Entry in the database
                selectedDay = dataBase.createDay();
                // Entry to the beginning of the displayed list
                daysListArrayAdapter.insert(selectedDay.getDate(), 0);
                displayDetailLayout();
                return true;
            case R.id.newEntry:
                Intent intent = new Intent(
                        getApplicationContext(), WriteEntry.class
                );
                intent.putExtra(EXTRA_WRITE_NOTE_NoteId, -1L);
                intent.putExtra(EXTRA_WRITE_NOTE_DayId, selectedDay.getId());
                startActivity(intent);
                return true;
	        case R.id.preferences:
	        	Intent preferences = new Intent(
                        getApplicationContext(),
                        Preferences.class
                );
	        	startActivity(preferences);
	        	return true;
	        case R.id.deleteDay:
	            if(selectedDay != null){
                    AlertDialog.Builder build = new AlertDialog.Builder(
                            MainActivity.this
                    );
                    build.setMessage("Are you sure?");
                    build.setNegativeButton("Delete Day", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dataBase.deleteDay(selectedDay);
                            selectedDay = null;
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Deleted",
                                    Toast.LENGTH_SHORT
                            ).show();
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    MainActivity.class
                            );
                            startActivity(intent);
                        }
                    });

                    build.setPositiveButton("Don't delete", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int id){
                        	// Nothing
                        }
                    });
                    AlertDialog alert = build.create();
                    alert.show();
	                return true;
	            }
	        case R.id.gallery:
	        	Intent gallery = new Intent(
                        getApplicationContext(),
                        GalleryActivity.class
                );
	        	startActivity(gallery);
	        	return true;
	    }
		return false;
	}
}
