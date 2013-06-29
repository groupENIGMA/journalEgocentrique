package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends Activity {

	private int textSize = -1;
	private String fontType = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setAdapters();
	}
	
	// Set the adapters for displaying the correct font size and type and the update interval for mood and notes
	private void setAdapters(){
		
		// Set the font type
		Spinner font = (Spinner)findViewById(R.id.textFont);
		final ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(this, R.array.textFont, android.R.layout.simple_spinner_item);
		fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		font.setAdapter(fontAdapter);
		   // Set the listener
        OnItemSelectedListener fontListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				fontType = fontAdapter.getItem(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// Nothing to do
			}
        };
        font.setOnItemSelectedListener(fontListener);
		
		// Set the font size
		Spinner size = (Spinner)findViewById(R.id.textSize);
		final ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.textSize, android.R.layout.simple_spinner_dropdown_item);
		size.setAdapter(sizeAdapter);
		   // Set the listener
        OnItemSelectedListener sizeListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
            	textSize = Integer.parseInt(sizeAdapter.getItem(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// Nothing to do
			}
        };
        size.setOnItemSelectedListener(sizeListener);
		
		Spinner updateInterval = (Spinner)findViewById(R.id.updateInterval);
		ArrayAdapter<CharSequence> updateAdapter = ArrayAdapter.createFromResource(this, R.array.updateInterval, android.R.layout.simple_spinner_dropdown_item);
		updateInterval.setAdapter(updateAdapter);
		//TODO-Listener alle liste che permettano di modificare realmente l'aspetto
	}
	
	public void send(View view){
    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    	intent.putExtra(MainActivity.EXTRA_SETTINGS_TextFont, fontType);
    	intent.putExtra(MainActivity.EXTRA_SETTINGS_TextSize, textSize);
    	startActivity(intent);
	}
}
