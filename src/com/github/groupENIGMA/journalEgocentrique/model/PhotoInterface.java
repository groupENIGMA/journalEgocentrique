package com.github.groupENIGMA.journalEgocentrique.model;

/**
 * This class is used by {@link DBInterface} to model the Photo
 * associated with a daily Entry.
 * <p>
 * You shouldn't create new instance of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DBInterface}
 * 
 * @version 0.1
 * @author groupEnigma
 *
 */
public interface PhotoInterface {

    /** Returns the path to the jpg file saved on external storage
     * 
     * @return the URI of the photo
     */
    public String getPath();
    
    /** Returns the path to the lower resolution version of the image.
     * 
     * @return the URI of the thumbnail of this photo
     */
     public String getPathThumb();
    
    /**
     * Checks if a Photo can be modified.
     * 
     * @return true if the Photo can be modified, false otherwise
     */
    public boolean canBeUpdated();
    
    /**
     * Checks if a Photo can be deleted.
     * 
     * @return true if the Photo can be deleted, false otherwise
     */
    public boolean canBeDeleted();
}
