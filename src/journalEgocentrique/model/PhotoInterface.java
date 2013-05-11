package journalEgocentrique.model;

public interface PhotoInterface {

/*
 * Costruttore non ammesso nelle interfacce
    // Crea una nuova foto
    protected Photo(File image);
 */   
    // Crea il thumbnail associato alla foto
    public Photo createThumb();
    
    // Controlla se la foto � modificabile
    public boolean canBeUpdated();
    
    // Controlla se la foto � eliminabile
    public boolean canBeDeleted();
}
