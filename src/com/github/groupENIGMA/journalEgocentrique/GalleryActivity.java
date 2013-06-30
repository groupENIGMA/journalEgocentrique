package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;

public class GalleryActivity extends Activity {

	private DB database;
	public final static String EXTRA_MESSAGE = "com.github.groupENIGMA.journalEgocentrique.MESSAGE";
	public final static String EXTRA_FILTERS_FilterPeriod = "filter_period";
	private SharedPreferences sharedPreferences;
	private String filterPeriod;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		database = new DB(getApplicationContext());
		database.open();
		
		// Open the shared preferences file
        sharedPreferences = getSharedPreferences(
                AppConstants.SHARED_PREFERENCES_FILENAME,
                MODE_PRIVATE
        );
        filterPeriod = sharedPreferences.getString(EXTRA_FILTERS_FilterPeriod, "None");
		GridView grid = (GridView)findViewById(R.id.galleryGrid);
		final GalleryAdapter imgAdapter = new GalleryAdapter(this, filterPeriod);
		grid.setAdapter(imgAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
				intent.putExtra(EXTRA_MESSAGE, imgAdapter.getItem(position).getPath());
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.gallery_option, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	case R.id.main:
	    		Intent main = new Intent(getApplicationContext(), MainActivity.class);
	    		startActivity(main);
	    		return true;
	    	case R.id.filters:
	    		Intent filters = new Intent(getApplicationContext(), Filters.class);
	    		startActivity(filters);
	    		return true;
	    }
	    return false;
	}
	
	/**
	 * Close the database connection before closing the Activity
	 */
	protected void onPause(){
		super.onPause();
		database.close();
	}
}
