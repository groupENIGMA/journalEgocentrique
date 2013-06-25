package com.github.groupENIGMA.journalEgocentrique;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

		ImageTapZoomView imageView = new ImageTapZoomView(this, getWindow()
				.getWindowManager().getDefaultDisplay().getOrientation());
		this.setContentView(imageView);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);

		ImagePinchZoomView impz = new ImagePinchZoomView(this);
		impz.setImageBitmap(bmp);
		impz.setMaxZoom(4f);
		setContentView(img);
	}
}
