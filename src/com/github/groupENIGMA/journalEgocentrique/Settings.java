package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends Activity {

	private int textSize = -1;
	private String fontType = null;
	private int timeout = -1;
	private SharedPreferences pref;
	private final String TEXT_SIZE = "text_size";
	private final String TEXT_FONT  = "text_font";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		pref = getSharedPreferences(
                AppConstants.SHARED_PREFERENCES_FILENAME,
                MODE_PRIVATE
        );
		textSize = pref.getInt(TEXT_SIZE, -1);
		fontType = pref.getString(TEXT_FONT, null);
		timeout = pref.getInt(AppConstants.PREFERENCES_KEY_ENTRY_TIMEOUT, -1);
		setAdapters();
	}
	
	// Set the adapters for displaying the correct font size and type and the update interval for mood and notes
	private void setAdapters(){
		
		// Set the font type
		Spinner font = (Spinner)findViewById(R.id.textFont);
		final ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(this, R.array.textFont, android.R.layout.simple_spinner_item);
		fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		font.setAdapter(fontAdapter);
		if(font != null){
			int spinnerPos = fontAdapter.getPosition(fontType);
			font.setSelection(spinnerPos);
		}
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
		if(textSize != -1){
			int spinnerPos = sizeAdapter.getPosition(String.valueOf(textSize));
			size.setSelection(spinnerPos);
		}
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
		final ArrayAdapter<CharSequence> updateAdapter = ArrayAdapter.createFromResource(this, R.array.updateInterval, android.R.layout.simple_spinner_dropdown_item);
		updateInterval.setAdapter(updateAdapter);
		if(timeout != -1){
			int spinnerPos = updateAdapter.getPosition(String.valueOf(timeout));
			updateInterval.setSelection(spinnerPos);
		}

		// Set the listener
		OnItemSelectedListener updateListener = new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				timeout = Integer.parseInt(updateAdapter.getItem(position).toString());
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				// Nothing to do
			}
		};
		updateInterval.setOnItemSelectedListener(updateListener);
	}
	
	public void send(View view){
    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    	SharedPreferences.Editor edit = pref.edit();
    	if(fontType != null)
    		edit.putString(TEXT_FONT, fontType);
    	if(textSize != -1)
    		edit.putInt(TEXT_SIZE, textSize);
    	if(timeout != -1)
    		edit.putInt(AppConstants.PREFERENCES_KEY_ENTRY_TIMEOUT, timeout);
    	edit.commit();
    	startActivity(intent);
	}
}
