package journalEgocentrique.model;

import java.util.Calendar;

public interface EntryInterface {

/*
 * non si può definire il costruttore in un'interfaccia, l'ho tenuto comunque a commento
    // Instanziata solo dal DataSource 
    protected Entry(Calendar day, long id){};
 */   
    // Restituisce la data della Entry
    public Calendar getDay();
    
    // Restituisce l'identificatore dell'Entry
    public long getId();

    // Controlla se la entry è eliminabile[entro la giornata]
    public boolean canBeDeleted();
    
    // Controlla se la entry è modificabile[entro la giornata]
    public boolean canBeUpdated();
}
