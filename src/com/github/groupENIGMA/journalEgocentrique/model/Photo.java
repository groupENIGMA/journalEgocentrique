package com.github.groupENIGMA.journalEgocentrique.model;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.github.groupENIGMA.journalEgocentrique.AppConstants;


public class Photo implements PhotoInterface {
    
    private String path;
    private String thumb_path;

    /**
     * Creates a Photo
     * <p>
     * This constructor should be used only by a {@link DBInterface}
     * implementation.
     *
     * @param photo_path The absolute path to the photo file.
     * @param thumb_path The absolute path to the thumb file
     */
    public Photo(String photo_path, String thumb_path) {
        this.path = path;
        this.thumb_path = thumb_path;
    }

    /**
     * Creates a Photo
     * <p>
     * This constructor should be used only by a {@link DBInterface}
     * implementation.
     *
     * @param photo_path The absolute path to the photo file. The thumb_path
     *        will be calculated from this and using the prefixes
     *        {@link AppConstants#PHOTO_FILENAME_PREFIX} and
     *        {@link AppConstants#THUMB_FILENAME_PREFIX}
     */
    public Photo(String photo_path) {
        this.path = photo_path;

        // Split the filename from the path to the photoDir
        int lastSeparator = photo_path.lastIndexOf(File.separator);
        String dirPath = photo_path.substring(0, lastSeparator+1);
        String fileName = photo_path.substring(lastSeparator+1);

        // Extract the Day's id from the filename
        int idStartIndex = AppConstants.PHOTO_FILENAME_PREFIX.length();
        String dayIdAndExtension = fileName.substring(idStartIndex);

        // Calculate the thumb path
        this.thumb_path =
            dirPath + AppConstants.THUMB_FILENAME_PREFIX + dayIdAndExtension;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getPathThumb() {
        return this.thumb_path;
    }
}
