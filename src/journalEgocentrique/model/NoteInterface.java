package journalEgocentrique.model;

/**
 * This class is used by {@link DataSourceInterface} to model a Note of
 * an Entry of the diary.
 * <p>
 * You shouldn't create new instance of this object using directly its
 * constructors: you should only use the instances returned by
 * the {@link DataSourceInterface}
 * 
 * @version 0.1
 * @author groupENIGMA
 *
 */
public interface NoteInterface {

    // Setta il testo
    public void setText(String new_text);
    
    // Ottieni il testo
    public String getText();
    
    // Ottieni l'id della Nota
    public long getId();

    /**
     * Checks if the Note can be updated.
     * <p>
     * A Note can be updated only during a grace period set by the final
     * user in the SharedPreferences
     * 
     * @return true if the Note can be updated, false otherwise
     */
    public boolean canBeUpdated();
    
    /**
     * Checks if the Note can be deleted 
     * <p>
     * A Note can be deleted only during a grace period set by the final
     * user in the SharedPreferences
     * 
     * @return true if the Note can be deleted, false otherwise
     */
    public boolean canBeDeleted();
    
}
