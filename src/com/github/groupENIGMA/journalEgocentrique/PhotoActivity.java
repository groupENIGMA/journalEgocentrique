package com.github.groupENIGMA.journalEgocentrique;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.groupENIGMA.journalEgocentrique.model.DB;
import com.github.groupENIGMA.journalEgocentrique.model.Day;
import com.github.groupENIGMA.journalEgocentrique.model.Photo;

public class PhotoActivity extends Activity {

    private static final String TAG_LOG = "PhotoActivity";
    private static final int CAMERA_REQUEST = 0;
    private static final String PREF_TEMP_ID = "temp_id";
    private DB dataBase;
    private ImageView photoPreviewView;
    private Day day;
    // The folder where all the Photo are saved
    private final String photoDirPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator +
            AppConstants.EXTERNAL_STORAGE_PHOTO_DIR;
    // The path to the temp Photo
    private String tmpPhotoPath = photoDirPath + File.separator + "tmp.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        dataBase = new DB(getApplicationContext());
        dataBase.open();

        // Create the photoDir if it doesn't exist
        File photoDir = new File(photoDirPath);
        if(!photoDir.exists() && !photoDir.mkdir()) {
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.PhotoActivity_ExternalStorageError),
                    Toast.LENGTH_SHORT
            ).show();
        }
        else {
            Log.v(TAG_LOG, "photoDir exists!");
        }

        // Get the old photo of the Day (if any)
        Intent received = getIntent();
        final long dayId = received.getLongExtra(
                MainActivity.EXTRA_PHOTO_ACTIVITY_DayId, -1L
        );
        day = dataBase.getDay(dayId);
        Photo dailyPhoto = day.getPhoto();

        // Display the preview of:
        photoPreviewView = (ImageView)findViewById(R.id.photo);
        // The temp photo
        File tmpPhotoFile = new File(tmpPhotoPath);
        if(tmpPhotoFile.exists() && isTempPhotoOfDay()) {
            photoPreviewView.setImageURI(Uri.fromFile(tmpPhotoFile));
        }
        // or the old Daily photo
        else if(dailyPhoto != null) {
            photoPreviewView.setImageURI(Uri.parse(dailyPhoto.getPath()));
            // Disable the Save button: no need to save again the same photo
            Button save = (Button) findViewById(R.id.save_photo);
            save.setEnabled(false);
        }
        // or, if none of the two above are available, the default Photo will be
        // rendered by the xml
        else {
            // Disable the setDefault and Save button.
            // They are useless when there's already the default Image
            Button setDefault = (Button) findViewById(R.id.setDefault);
            setDefault.setEnabled(false);
            Button save = (Button) findViewById(R.id.save_photo);
            save.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-open database connection if it was closed by onPause()
        if (!dataBase.isOpen()) {
            dataBase.open();
        }
        // If the day "expired" when the Activity was suspended return to the
        // main Activity
        if (day != null && !day.canBeUpdated()) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBase.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        // If the user took a new Photo save it
        if (resultCode == RESULT_OK ) {
            Log.v(TAG_LOG, "Photo correctly returned by Camera App!");
            // Update the preview with the new Photo.
            // I'm using setImageBitmap instead of setImageURI because the
            // former doesn't always works on some devices
            photoPreviewView.setImageBitmap(
                    BitmapFactory.decodeFile(tmpPhotoPath)
            );
            // Enable the setDefault and Save button.
            // They can be disabled if previously there was the default Image
            Button setDefault = (Button) findViewById(R.id.setDefault);
            setDefault.setEnabled(true);
            Button save = (Button) findViewById(R.id.save_photo);
            save.setEnabled(true);
            // Update the id of saved temp photo
            updateTempPhotoId();
        }
        else if (resultCode == RESULT_CANCELED) {
            // Don't save the photo
            Log.v(TAG_LOG, "Camera app closed without taking a new Photo!");
        }
        else {
            Log.e(TAG_LOG, resultCode + "");
            Log.e(TAG_LOG, "The Camera app didn't save the Photo!");
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.PhotoActivity_ExternalStorageError),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
	
	/**
	 *  Remove the daily photo and sets the default avatar
	 *  The button must be enabled ONLY IF the actual image isn't the default avatar
	 */
	public void removeImage(View view){
        clearTempPhotoId();
		dataBase.removePhoto(day);
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Accept the displayed photo as the new day-photo.
	 * @param view
	 */
	public void accept(View view){
        // Clear the tempPhotoId (tempPhoto will be moved by DB.setDayPhoto)
        clearTempPhotoId();
		dataBase.setDayPhoto(day, tmpPhotoPath);
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}

    /**
     * Removes the unsaved tmp photo when back is pressed
     */
    @Override
    public void onBackPressed() {
        // The user discarded any taken photo, clear the temp Photo id
        clearTempPhotoId();
        super.onBackPressed();
    }

    /**
     * Launch an Intent to the default Camera application
     * to take another picture.
     * The Picture will be saved to
     */
    public void takePicture(View view) {
        // Firstly create the tmpPhotoFile if needed
        File tmpPhotoFile = new File(tmpPhotoPath);
        if (!tmpPhotoFile.exists()) {
            try {
                tmpPhotoFile.createNewFile();
            }
            catch (IOException e) {
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.PhotoActivity_ExternalStorageError),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
        // Start the Camera app
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(tmpPhotoFile)
        );
        startActivityForResult(camera, CAMERA_REQUEST);
    }

    /**
     * Checks if the temp photo saved in the External Storage is of Day
     *
     * @return true if the saved photo is of day, false if the photo is of
     *         a previous day
     */
    private boolean isTempPhotoOfDay() {
        // Get the date of the saved temp photo
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        long tempId = pref.getLong(
                PREF_TEMP_ID,
                -1L
        );
        return (tempId == day.getId());
    }

    /**
     * Sets the id of temp photo saved in the preferences to -1 (temp photo not
     * valid)
     */
    private void clearTempPhotoId() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(PREF_TEMP_ID, -1L);
        edit.commit();
    }

    /**
     * Sets the id of temp photo saved in the preferences to day.id
     */
    private void updateTempPhotoId() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(PREF_TEMP_ID, day.getId());
        edit.commit();
    }
}
