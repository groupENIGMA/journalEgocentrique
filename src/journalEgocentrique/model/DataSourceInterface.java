package journalEgocentrique.model;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import android.database.SQLException;

public interface DataSourceInterface {

/*
 * Costruttore non ammesso
    // Inizializza l'oggetto
    public DataSource(Context context) {};
*/
	
    // Apre la connessione al database
    // se non riesce lancia SQLException
    public void open() throws SQLException;
    
    // Chiude la connessione
    public void close();

    // Restituisce l'entry di oggi.
    // Se non esiste ne crea una e la restituisce
    public Entry getEntryOfTheDay();
    
    // Restituisce l'entry del giorno day.
    // Se non esiste ne crea una e la restituisce
    public Entry getEntryOfTheDay(Calendar day);
    
    // Restituisce la lista di tutte le entry
    // Se non esiste nessuna entry restituisce una lista vuota
    public List<Entry> getEntries();
    
    // Inserisce una nuova nota nell'Entry indicata
    // Se la data attuale è diversa dalla data dell'entry lancia InvalidOperationException
    public void insertNote(Entry entry, String note_text) throws InvalidOperationException;
    
    // Modifica la nota solo se la data attuale è nei limiti impostati
    // rispetto a quella dell'entry associata
    // altrimenti lancia InvalidOperationException
    public void updateNote(Note note) throws InvalidOperationException;
    
    // Rimuove la nota dal database
    // Se non esiste lancia InvalidOperationException
    public void deleteNote(Note note) throws InvalidOperationException;
    
    // Restituisce la/le note associate all'Entry
    // se non presenti restituisce una lista vuota
    public List<Note> getNotes(Entry entry);
    
    // Imposta l'umore del giorno, se già presente lo sostituisce
    // Se la data attuale è diversa dalla data dell'Entry restituisce
    // InvalidOperationException
    public void setMood(Entry entry, Mood mood) throws InvalidOperationException;
    
    // Restituisce l'umore del giorno, null se non è stato impostato
    public Mood getMood(Entry entry);
    
    // Rimuove l'umore del giorno sostituendolo con null
    public void removeMood(Entry entry);
    
    // Imposta come foto del giorno per entry la foto disponibile
    // al path_to_foto
    // Se la entry ha già una foto, quella vecchia sarà cancellata.
    // Se la foto non è modificabile (solo le foto scattate nella giornata possono essere rimosse)
    // restituisce InvalidOperationException
    public Photo setPhoto(Entry entry, File photo) throws InvalidOperationException;

    // Rimuove la foto dal database
    // Se la foto non è rimuovibile (solo le foto scattate nella giornata possono essere rimosse)
    // lancia InvalidOperationException
    public void deletePhoto(Photo photo) throws InvalidOperationException;
    
    // Restituisce TUTTE le foto presenti nel database
    public List<Photo> getPhotos();
    
    // Restituisce le foto presenti nel database scattate
    // nell'arco di tempo compreso tra to e from
    public List<Photo> getPhotos(Calendar from, Calendar to);
    
}
