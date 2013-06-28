package com.github.groupENIGMA.journalEgocentrique;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;

public class ListActivity extends Activity {

    public final static String EXTRA_WRITENOTE_NoteId = "NoteId";
    public final static String EXTRA_WRITENOTE_DayId = "EntryId";
    public final static String EXTRA_MESSAGE = "com.github.groupENIGMA.journalEgocentrique.MESSAGE";

    private final static String PREF_SELECTED_ENTRY = "selectedEntry_id";

    private List<Calendar> daysList;
    private DB dataBase;
    private Day selectedDay = null;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        setView();
        dataBase = new DB(getApplicationContext());

        // Open the shared preferences file
        sharedPreferences = getSharedPreferences(
                AppConstants.SHARED_PREFERENCES_FILENAME,
                MODE_PRIVATE
        );
    }
    
    /**
     * Sets dinamically proportioned the size of the Entries, Images and Notes
     */
    private void setView(){
    	Display display = getWindowManager().getDefaultDisplay();
    	int width = display.getWidth();
    	int height = display.getHeight();
    	ListView list = (ListView)findViewById(R.id.list);
    	ImageView photo = (ImageView)findViewById(R.id.dailyPhoto);
    	ListView notes = (ListView)findViewById(R.id.notes);
    	FrameLayout frame = (FrameLayout)findViewById(R.id.frameLayout);
    	// Set the ListView size
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)list.getLayoutParams();
    	params.height = height;
    	params.width = width/4;
    	list.setLayoutParams(params);
    	// Set the FrameLayout
    	params = (RelativeLayout.LayoutParams)frame.getLayoutParams();
    	params.height = height;
    	params.width = width*3/4;
    	frame.setLayoutParams(params);
    	// Set the photo
    	FrameLayout.LayoutParams param = (FrameLayout.LayoutParams)photo.getLayoutParams();
    	param.width = width/2;
    	param.height = height/2;
    	photo.setLayoutParams(param);
    	// Set the notes
    	param = (FrameLayout.LayoutParams)notes.getLayoutParams();
    	param.width = width/4;
    	param.height = height;
    	notes.setLayoutParams(param);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open database connection
        dataBase.open();
        // Display the list of days with an Day
        daysList = dataBase.getDatesList();
        ListView daysListView = (ListView)findViewById(R.id.list);
        displayDaysList(daysListView, daysList);

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
    }

    /**
     * Display the list of all Dates having an associated Day
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
                selectedDay = dataBase.getDay(
                        (Calendar) adapter.getItemAtPosition(position)
                );
                // Refresh notes and images
                displayImages();
                // Display the Notes
                ListView notesListView = (ListView)findViewById(R.id.notes);
                displayNotes(notesListView);
            }
        };
        list.setOnItemClickListener(clickListener);
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
        OnItemClickListener clickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                int position, long id) {
                // Enable the onLongClickListener only if the Entry can be
                // updated.
                final Entry selectedNote = (Entry) adapter.getItemAtPosition(position);
                if (selectedNote.canBeUpdated(sharedPreferences)) {
                	AlertDialog.Builder build = new AlertDialog.Builder(ListActivity.this);
                	build.setMessage("Select the action");
                	build.setNegativeButton("Delete note", new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int id) {
							dataBase.deleteEntry(selectedNote);
							Toast toast = Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG);
							toast.show();
							// fare refresh schermata!
						}	
                	});
                	build.setPositiveButton("Update note", new OnClickListener(){
                		
                		@Override
                		public void onClick(DialogInterface dialog, int id){
                			Intent intent = new Intent(
                                    getApplicationContext(), WriteNote.class
                            );
                            intent.putExtra(
                                    EXTRA_WRITENOTE_NoteId, selectedNote.getId()
                            );
                            startActivity(intent);
                		}
                	});
                	AlertDialog alert = build.create();
                	alert.show();
                }
            }
        };
        list.setOnItemClickListener(clickListener);
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
	        case R.id.newDay:
	            selectedDay = dataBase.createDay();
	            daysList = dataBase.getDatesList();
	    	    ListView list = (ListView)findViewById(R.id.list);
	    	    displayDaysList(list, daysList);
	            return true;
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
