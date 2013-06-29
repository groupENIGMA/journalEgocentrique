package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		database = new DB(getApplicationContext());
		database.open();
		GridView grid = (GridView)findViewById(R.id.galleryGrid);
		final GalleryAdapter imgAdapter = new GalleryAdapter(this);
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
    protected void onResume() {
        super.onResume();
        // Database connection must be reopened if the app was previously
        // "paused" with onPause()
        if (!database.isOpen()) {
            database.open();
        }
    }

    /**
     * Close the database connection before closing the Activity
     */
    protected void onPause(){
        super.onPause();
        database.close();
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
	    	case R.id.settings:
	    		Intent settings = new Intent(getApplicationContext(), Settings.class);
	    		startActivity(settings);
	    		return true;
	    }
	    return false;
	}
}
