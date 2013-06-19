package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Entry;
import com.github.groupENIGMA.journalEgocentrique.model.Photo;

public class GalleryActivity extends Activity {

	private DB database;
	private Entry myEntry;
	public final static String EXTRA_MESSAGE = "com.github.groupENIGMA.journalEgocentrique.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		database = new DB(getApplicationContext());
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

}
