package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class FullScreenPhoto extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_photo);
		Intent intent = getIntent();
		String path = intent.getStringExtra("Path");
		ImageView img = (ImageView) findViewById(R.id.photo);
		img.setImageURI(Uri.parse(path));
	}
}
