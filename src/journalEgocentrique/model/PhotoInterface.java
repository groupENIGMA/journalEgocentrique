package journalEgocentrique.model;

/**
 * This class is used by {@link DataSourceInterface} to model the Photo
 * associated with a daily Entry.
 * <p>
 * You shouldn't create new instance of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DataSourceInterface}
 * 
 * @version 0.1
 * @author groupEnigma
 *
 */
public interface PhotoInterface {

    /**
     * Create the thumbnail of the Photo
     * 
     * @return
     */
    public Photo createThumb();
    
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
