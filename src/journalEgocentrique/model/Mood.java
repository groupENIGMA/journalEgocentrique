package journalEgocentrique.model;

public class Mood {
	
	protected final String[] moods = {"FELICE", "TRISTE", "INDIFFERENTE", "ARRABBIATO", "SVOGLIATO", "ENTUSIASTA"};
    private int id;
    private int drawable;
	
    // Costruttore Mood
    public Mood(int id, int drawable) {
        this.id = id;
        this.drawable = drawable;
    }
    
    // Ritorna l'id del Mood
    public int getId() {return this.id;}
    
    // Ritorna drawable del Mood
    public int getDrawable() {return this.drawable;}
    
}
