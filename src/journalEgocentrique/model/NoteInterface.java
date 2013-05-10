package journalEgocentrique.model;


public interface NoteInterface {

/*
 * Anche qui, il costruttore non è definibile tramite interfaccia
    // Costruttore di una nuova nota,
    // associa un serial riferito alla entry(num di note presenti)
    protected Note(String text, long id) {};
*/    
    // Setta il testo
    public void setText(String new_text);
    
    // Ottieni il testo
    public String getText();
    
    // Ottieni l'id della Nota
    public long getId();
    
    // Controlla se la nota passata per parametro è uguale alla nota attuale
    // true se uguali, false altrimenti
    public boolean equals(Note note); // NB due note sono uguali se hanno id e testo uguale

    // Controlla se la nota è modificabile secondo i parametri impostati dall'utente
    public boolean canBeUpdated();
    
    // Controlla se la nota è cancellabile secondo i parametri impostati dall'utente
    public boolean canBeDeleted();
    
}
