package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setAdapters();
	}
	
	// Set the adapters for displaying the correct font size and type and the update interval for mood and notes
	private void setAdapters(){
		Spinner font = (Spinner)findViewById(R.id.textFont);
		ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(this, R.array.textFont, android.R.layout.simple_spinner_item);
		fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		font.setAdapter(fontAdapter);
		
		Spinner size = (Spinner)findViewById(R.id.textSize);
		ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.textSize, android.R.layout.simple_spinner_item);
		size.setAdapter(sizeAdapter);
		
		Spinner updateInterval = (Spinner)findViewById(R.id.updateInterval);
		ArrayAdapter<CharSequence> updateAdapter = ArrayAdapter.createFromResource(this, R.array.updateInterval, android.R.layout.simple_spinner_item);
		updateInterval.setAdapter(updateAdapter);
		//TODO-Listener alle liste che permettano di modificare realmente l'aspetto
	}
}
