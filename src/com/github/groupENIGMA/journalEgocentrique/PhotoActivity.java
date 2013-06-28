package com.github.groupENIGMA.journalEgocentrique;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Photo;

public class PhotoActivity extends Activity {

	private static final int CAMERA_REQUEST = 1; 
	private DB data;
	private ImageView actualImg;
	private Bitmap mImageBitmap;
	private Day day;
	private File tmpImg;
	private String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
			File.separator + AppConstants.EXTERNAL_STORAGE_PHOTO_DIR + "~temp.jpg";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		setView();
		actualImg = (ImageView)findViewById(R.id.photo);
		data = new DB(getApplicationContext());
		Intent received = getIntent();
		data.open();
		final long dayId = received.getLongExtra(
                MainActivity.EXTRA_PHOTO_ACTIVITY_DayId, 0
        );
		day = data.getDay(dayId);
		Photo tmp = day.getPhoto();
		tmpImg = new File(tempPath);
		if(tmpImg.exists())
			actualImg.setImageURI(Uri.parse(tempPath));
		else if(tmp != null)
			actualImg.setImageURI(Uri.parse(tmp.getPath()));
	}
	
    /**
     * Sets dinamically proportioned the size of the Entries, Images and Notes
     */
    private void setView(){
    	Display display = getWindowManager().getDefaultDisplay();
    	int width = display.getWidth();
    	int height = display.getHeight();
    	
    	ImageView photo = (ImageView)findViewById(R.id.photo);
    	LinearLayout list = (LinearLayout)findViewById(R.id.linear_layout);
    	Button button = (Button)findViewById(R.id.button);
    	
    	// Set the photo
    	FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)photo.getLayoutParams();
    	params.width = width * 3 / 4;
    	photo.setLayoutParams(params);
    	// Set the list
    	params = (FrameLayout.LayoutParams)list.getLayoutParams();
    	params.width = width/6;
    	list.setLayoutParams(params);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		handleCameraPhoto(data);
	}
	
	private void handleCameraPhoto(Intent intent) {
	    Bundle extras = intent.getExtras();
	    mImageBitmap = (Bitmap) extras.get("data");
	    actualImg.setImageBitmap(mImageBitmap);
	    
	       //Gets the path and the directory name where the Photo is going to be saved
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File photoDir = new File(
                path + File.separator + AppConstants.EXTERNAL_STORAGE_PHOTO_DIR
        );
        //Creates the file
        File file = new File (tempPath);
        //Delete if already exists
        if (file.exists ()) {
            file.delete ();
        }
        if (! photoDir.exists()){
            photoDir.mkdirs();
        }
        //Writes the file with the picture in the selected path
        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
               e.printStackTrace();
        }
	}
	
	/**
	 *  Remove the daily photo and sets the default avatar
	 *  The button must be enabled ONLY IF the actual image isn't the default avatar
	 */
	public void removeImage(View view){
		DB data = new DB(getApplicationContext());
		data.open();
		data.removePhoto(day);
		actualImg.setImageResource(R.drawable.ic_launcher);
	}
	
	/**
	 * Accept the displayed photo as the new day-photo.
	 * @param view
	 */
	public void accept(View view){
		data.setDayPhoto(day, mImageBitmap);
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		data.close();
		if(tmpImg.exists())
			tmpImg.delete();
		startActivity(intent);
	}
	
	/**
	 * Launch an Intent to the default Camera application
	 * to take another picture
	 */
	public void takePicture(View view){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAMERA_REQUEST);
	}
	
	protected void onPause(){
		super.onPause();
		data.close();
	}

}
